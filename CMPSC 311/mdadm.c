// #include <assert.h>
// #include <math.h>
// #include <stdint.h>
// #include <stdio.h>
// #include <stdlib.h>
// #include <string.h>

// #include "cache.h"
// #include "jbod.h"
// #include "mdadm.h"

// int is_mounted = 0;
// int is_written = 0;


// int mdadm_mount(void) {
// 	return -1;
// }

// int mdadm_unmount(void) {
// 	return -1;
// }

// int mdadm_write_permission(void){
//   return -1;
// }


// int mdadm_revoke_write_permission(void){
//   return -1;
// }


// int mdadm_read(uint32_t addr, uint32_t len, uint8_t *buf) {
//   return -1;
// }

// int mdadm_write(uint32_t addr, uint32_t len, const uint8_t *buf) {
//   return -1;
// }

#include <assert.h>
#include <math.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "cache.h"
#include "jbod.h"
#include "mdadm.h"
#include "net.h"

int is_mounted = 0;
int is_written = 0;

int mdadm_mount(void) {
  if (is_mounted == 1) {
    return -1;
  }
  
  uint32_t op = (JBOD_MOUNT << 12);
  
  if (jbod_client_operation(op, NULL) == 0) {
    is_mounted = 1;
    return 1;
  }
  
  return -1;
}

int mdadm_unmount(void) {
  if (is_mounted == 0) {
    return -1;
  }
  
  uint32_t op = (JBOD_UNMOUNT << 12);
  
  if (jbod_client_operation(op, NULL) == 0) {
    is_mounted = 0;
    return 1;
  }
  
  return -1;
}

int mdadm_write_permission(void) {
  uint32_t op = (JBOD_WRITE_PERMISSION << 12);

  if (jbod_client_operation(op, NULL) == 0) {
    is_written = 1;
    return 1;
  }

  return -1;
}

int mdadm_revoke_write_permission(void) {
  uint32_t op = (JBOD_REVOKE_WRITE_PERMISSION << 12);

  if (jbod_client_operation(op, NULL) == 0) {
    is_written = 0;
    return 1;
  }

  return -1;
}

int mdadm_read(uint32_t addr, uint32_t len, uint8_t *buf) {
  if (is_mounted == 0) {
    return -3;
  }

  if (len > 1024) {
    return -2; 
  }
  
  if (len > 0 && buf == NULL) {
    return -4;
  }
  
  if (addr + len > JBOD_DISK_SIZE * JBOD_NUM_DISKS) {
    return -1;  
  }
  
  uint32_t byte_read = 0;
  uint32_t current_addr = addr;
  
  while (byte_read < len) {
    uint32_t disk_id = current_addr / JBOD_DISK_SIZE;
    uint32_t block_id = (current_addr % JBOD_DISK_SIZE) / JBOD_BLOCK_SIZE;
    uint32_t offset = (current_addr % JBOD_DISK_SIZE) % JBOD_BLOCK_SIZE;

    uint8_t block_data[JBOD_BLOCK_SIZE];
    int cache_hit = cache_lookup(disk_id, block_id, block_data);

    if (cache_hit != 1) {
      uint32_t seek_disk_op = (JBOD_SEEK_TO_DISK << 12) | disk_id;
      if (jbod_client_operation(seek_disk_op, NULL) == -1) {
        return -1;
      }
      
      uint32_t seek_block_op = (JBOD_SEEK_TO_BLOCK << 12) | (block_id & 0xFF) << 4;
      if (jbod_client_operation(seek_block_op, NULL) != 0) {
        return -1;
      }
      
      uint32_t read_block_op = (JBOD_READ_BLOCK << 12);
      if (jbod_client_operation(read_block_op, block_data) == -1) {
        return -1;
      }

      cache_insert(disk_id, block_id, block_data);
    }

    uint32_t copy_len = JBOD_BLOCK_SIZE - offset;
    if (copy_len > len - byte_read) {
      copy_len = len - byte_read;
    }
    
    memcpy(buf + byte_read, block_data + offset, copy_len);
    byte_read += copy_len;
    current_addr += copy_len;
  }
  
  return byte_read;
}

int mdadm_write(uint32_t addr, uint32_t len, const uint8_t *buf) {
  if (is_mounted == 0) {
    return -3;
  }

  if (is_written == 0) {
    return -5;
  }

  if (len > 1024) {
    return -2;
  }

  if (len > 0 && buf == NULL) {
    return -4;
  }

  if (addr + len > JBOD_DISK_SIZE * JBOD_NUM_DISKS) {
    return -1;
  }

  uint32_t total_written_data = 0;

  while (total_written_data < len) {  
    uint32_t current_address = addr + total_written_data;
    uint32_t current_diskID = current_address / JBOD_DISK_SIZE;
    uint32_t current_blockID = (current_address % JBOD_DISK_SIZE) / JBOD_BLOCK_SIZE;

    uint8_t temp_copied_block_data[JBOD_BLOCK_SIZE];

    if (cache_lookup(current_diskID, current_blockID, temp_copied_block_data) != 1) {
      uint32_t seek_disk_op = (JBOD_SEEK_TO_DISK << 12) | current_diskID;
      jbod_client_operation(seek_disk_op, NULL);

      uint32_t seek_block_op = (JBOD_SEEK_TO_BLOCK << 12) | (current_blockID & 0xFF) << 4;
      jbod_client_operation(seek_block_op, NULL);

      jbod_client_operation(JBOD_READ_BLOCK << 12, temp_copied_block_data);

      cache_insert(current_diskID, current_blockID, temp_copied_block_data);
    }

    uint32_t start_address_of_current_block = (current_diskID * JBOD_DISK_SIZE) + (current_blockID * JBOD_BLOCK_SIZE);
    uint32_t start_writing_byte = (current_address - start_address_of_current_block);
    uint32_t bytes_left_in_block = JBOD_BLOCK_SIZE - start_writing_byte;

    if ((len - total_written_data) >= (bytes_left_in_block)) {
      memcpy(temp_copied_block_data + start_writing_byte, buf + total_written_data, bytes_left_in_block);
      total_written_data += bytes_left_in_block;
    } else {
      memcpy(temp_copied_block_data + start_writing_byte, buf + total_written_data, len - total_written_data);
      total_written_data += (len - total_written_data);
    }

    cache_update(current_diskID, current_blockID, temp_copied_block_data);

    uint32_t seek_disk_op = (JBOD_SEEK_TO_DISK << 12) | current_diskID;
    jbod_client_operation(seek_disk_op, NULL);

    uint32_t seek_block_op = (JBOD_SEEK_TO_BLOCK << 12) | (current_blockID & 0xFF) << 4;
    jbod_client_operation(seek_block_op, NULL);

    jbod_client_operation(JBOD_WRITE_BLOCK << 12, temp_copied_block_data);
  }

  return len;
}
