// #include <stdint.h>
// #include <stdlib.h>
// #include <string.h>
// #include <stdio.h>
// #include <assert.h>

// #include "cache.h"
// #include "jbod.h"

// //Uncomment the below code before implementing cache functioncs.
// /*static cache_entry_t *cache = NULL;
// static int cache_size = 0;
// static int clock = 0;*/
// static int num_queries = 0;
// static int num_hits = 0;

// int cache_create(int num_entries) {
//     return -10;
// }

// int cache_destroy(void) {
//   return -10;
// }

// int cache_lookup(int disk_num, int block_num, uint8_t *buf) {
//   return -10;
// }

// void cache_update(int disk_num, int block_num, const uint8_t *buf) {
// }

// int cache_insert(int disk_num, int block_num, const uint8_t *buf) {
//   return -10;
// }

// bool cache_enabled(void) {
//   return false;
// }

// void cache_print_hit_rate(void) {
// 	fprintf(stderr, "num_hits: %d, num_queries: %d\n", num_hits, num_queries);
//   fprintf(stderr, "Hit rate: %5.1f%%\n", 100 * (float) num_hits / num_queries);
// }

// int cache_resize(int new_size) {
//    return -1;
// }



#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

#include "cache.h"
#include "jbod.h"

//Uncomment the below code before implementing cache functions.
static cache_entry_t *cache = NULL;
static int cache_size = 0;
static int clock = 0;
static int num_queries = 0;
static int num_hits = 0;

// int cache_create(int num_entries) {
//     if (cache != NULL) {
//       return -1;
//     }
//     if (num_entries < 2 || num_entries > 4096 ) {
//       return -1;
//     }
//     cache = (cache_entry_t *)calloc(num_entries, sizeof(cache_entry_t));
//     if (cache == NULL){
//       return -1;
//     }
//     cache_size = num_entries;
//     // clock = 0; // Initialize clock
//     // num_queries = 0;
//     // num_hits = 0;
//     // fprintf(stderr, "Cache created with size: %d\n", num_entries);
//     return 0;
// }

int cache_create(int num_entries) {

    // Check if cache already exists
    if (cache != NULL) {
        return -1;
    }

    // Validate num_entries range
    if (num_entries < 2 || num_entries > 4096) {
        return -1;
    }

    // Allocate memory for the cache
    cache = calloc(num_entries, sizeof(cache_entry_t));
    if (cache == NULL) {
        return -1;
    }

    // Initialize cache entries
    for (int i = 0; i < num_entries; i++) {
        cache[i].valid = false;
        cache[i].clock_accesses = 0;
    }

    cache_size = num_entries;
    clock = 0;  // Initialize clock
    num_queries = 0;
    num_hits = 0;
    return 1;
}


int cache_destroy(void) {
  if (cache == NULL) {
    return -1;
  }
  free(cache);
  cache = NULL;
  cache_size = 0;
  clock = 0;
  return 0;
}

int cache_lookup(int disk_num, int block_num, uint8_t *buf) {
  if(cache == NULL || buf == NULL) {
    return -1;
  }
  if (block_num < 0 || block_num >= 256 || disk_num < 0 || disk_num >= 16) {
    return -1;
  }
  num_queries++;
  for (int i=0; i < cache_size; i++){
    if (cache[i].valid && cache[i].disk_num == disk_num && cache[i].block_num == block_num) {
            memcpy(buf, cache[i].block, JBOD_BLOCK_SIZE);
            cache[i].clock_accesses = ++clock;
            num_hits++;
            return 1;
        }
    }
    return -1;
}

// int cache_lookup(int disk_num, int block_num, uint8_t *buf) {
//     if (buf == NULL || !cache_enabled()) {
//         return -1;
//     }

//     // Increment query count
//     num_queries++;

//     for (int i = 0; i < cache_size; i++) {
//         if (cache[i].valid &&
//             cache[i].disk_num == disk_num &&
//             cache[i].block_num == block_num) {
//             // Cache hit: copy data to buffer
//             memcpy(buf, cache[i].block, JBOD_BLOCK_SIZE);

//             // Increment hit count and update clock_accesses
//             num_hits++;
//             cache[i].clock_accesses = ++clock;
//             return 1;
//         }
//     }

//     // Cache miss
//     return -1;
// }

void cache_update(int disk_num, int block_num, const uint8_t *buf) {
  if(cache != NULL){
    for(int i = 0; i < cache_size; i++){
      if (cache[i].valid == true && cache[i].disk_num == disk_num && cache[i].block_num == block_num){
        memcpy(cache[i].block, buf, JBOD_BLOCK_SIZE);
        cache[i].clock_accesses = clock;
      }
    }
  }
}

int cache_insert(int disk_num, int block_num, const uint8_t *buf) {
  if (cache == NULL || buf == NULL) {
    return -1;
  }

  // Validate disk_num and block_num
  if (block_num < 0 || block_num >= 256 || disk_num < 0 || disk_num >= 16) {
    return -1;
  }

  // Check for an empty slot or duplicate entry
  for (int i = 0; i < cache_size; i++) {
    if (!cache[i].valid) { // Empty slot found
      cache[i].disk_num = disk_num;
      cache[i].block_num = block_num;
      memcpy(cache[i].block, buf, JBOD_BLOCK_SIZE);
      cache[i].clock_accesses = ++clock; // Update access time
      cache[i].valid = true;
      return 1;
    }
    // Duplicate entry check
    if (cache[i].valid && cache[i].disk_num == disk_num && cache[i].block_num == block_num) {
      return -1; // Block already exists in cache
    }
  }

  // If no empty slot, implement MRU eviction policy
  int mru_index = 0; // Index of the most recently used block
  int highest_access_time = cache[0].clock_accesses;

  for (int i = 1; i < cache_size; i++) {
    if (cache[i].clock_accesses > highest_access_time) {
      highest_access_time = cache[i].clock_accesses;
      mru_index = i;
    }
  }

  // Evict the MRU entry and insert the new block
  cache[mru_index].disk_num = disk_num;
  cache[mru_index].block_num = block_num;
  memcpy(cache[mru_index].block, buf, JBOD_BLOCK_SIZE);
  cache[mru_index].clock_accesses = ++clock; // Update access time
  cache[mru_index].valid = true;

  return 1; 
}

bool cache_enabled(void) {
  return cache != NULL;
}

void cache_print_hit_rate(void) {
	fprintf(stderr, "num_hits: %d, num_queries: %d\n", num_hits, num_queries);
  fprintf(stderr, "Hit rate: %5.1f%%\n", 100.0 * num_hits / num_queries);
}

int cache_resize(int new_num_entries) {
   if (new_num_entries < 2 || new_num_entries > 4096 || cache == NULL) {
        return -1;
    }
    cache_entry_t *new_cache = malloc(sizeof(cache_entry_t) * new_num_entries);
    if (new_cache == NULL) {
        return -1;
    }
    int copy_size = new_num_entries < cache_size ? new_num_entries : cache_size;
    memcpy(new_cache, cache, sizeof(cache_entry_t) * copy_size);
    free(cache);
    cache = new_cache;
    cache_size = new_num_entries;
    return 0;

  // if (new_num_entries < 2 || new_num_entries > (JBOD_BLOCK_SIZE * JBOD_NUM_DISKS))
  // {
  //   // Invalid number of entries
  //   return -1;
  // }

  // // Allocate memory for the new cache with the specified number of entries
  // cache_entry_t *new_cache = malloc(new_num_entries * sizeof(cache_entry_t));
  // if (new_cache == NULL)
  // {
  //   // Memory allocation failure
  //   return -1;
  // }

  // // Initialize the new cache
  // int i = 0;
  // while (i < new_num_entries)
  // {
  //   new_cache[i].clock_accesses = 0;
  //   new_cache[i].valid = false;
  //   i++;
  // }

  // // Copy valid entries from the old cache to the new cache
  // int min_size = cache_size < new_num_entries ? cache_size : new_num_entries;
  // i = 0;
  // while (i < min_size)
  // {
  //   if (cache[i].valid)
  //   {
  //     new_cache[i] = cache[i];
  //   }
  //   i++;
  // }

  // // Free the memory occupied by the old cache
  // free(cache);

  // // Update the cache pointer and size
  // cache = new_cache;
  // cache_size = new_num_entries;

  // return 1;
}