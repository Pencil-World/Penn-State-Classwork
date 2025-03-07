// #include <stdbool.h>
// #include <stdlib.h>
// #include <string.h>
// #include <unistd.h>
// #include <stdio.h>
// #include <errno.h>
// #include <err.h>
// #include <sys/socket.h>
// #include <sys/types.h>
// #include <arpa/inet.h>
// #include "net.h"
// #include "jbod.h"

// /* the client socket descriptor for the connection to the server */
// int cli_sd = -1;

// /* attempts to read n bytes from fd; returns true on success and false on
//  * failure */
// bool nread(int fd, int len, uint8_t *buf) {
//   return false;
// }

// /* attempts to write n bytes to fd; returns true on success and false on
//  * failure */
// bool nwrite(int fd, int len, uint8_t *buf) {
//   return false;
// }

// /* attempts to receive a packet from fd; returns true on success and false on
//  * failure */
// bool recv_packet(int fd, uint32_t *op, uint8_t *ret, uint8_t *block) {
//   return false;
// }

// /* attempts to send a packet to sd; returns true on success and false on
//  * failure */
// bool send_packet(int fd, uint32_t op, uint8_t *block) {
//   return false;
// }

// /* connect to server and set the global client variable to the socket */
// bool jbod_connect(const char *ip, uint16_t port) {
//   return false;
// }

// void jbod_disconnect(void) {
  
// }

// int jbod_client_operation(uint32_t op, uint8_t *block) {
  
//   return -1;
// }


#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <err.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include "net.h"
#include "jbod.h"

/* the client socket descriptor for the connection to the server */
int cli_sd = -1;

/* attempts to read n bytes from fd; returns true on success and false on failure */
bool nread(int fd, int len, uint8_t *buf) {
  int fdRead = 0; //holds number of bytes read
  while (fdRead < len){ //loop continues until we read len bytes
    int result = read(fd, &buf[fdRead], len - fdRead); //read from buffer
    
    if (result < 0){
      printf("err");
      return false;
    }
    else{
      fdRead += result; //increment bytes read
    }
  }
  return true;
}

/* attempts to write n bytes to fd; returns true on success and false on failure */
bool nwrite(int fd, int len, uint8_t *buf) {
  int fdWrite = 0; //holds number of bytes written

  while(fdWrite < len){ //loop continues until we write len bytes
    int result = write(fd, &buf[fdWrite], len - fdWrite);
    if (result < 0){
      printf("error with nwrite");
      return false;
    }
    else{
      fdWrite += result; //increment bytes written
    }
  }
  return true;
}

/* attempts to receive a packet from fd; returns true on success and false on failure */
bool recv_packet(int fd, uint32_t *op, uint8_t *ret, uint8_t *block) {
  uint8_t header[HEADER_LEN];

  //read the packet header first
  if (nread(fd, HEADER_LEN, header) == false){
    printf("error");
    return false;
  }

  //converts between host and network byte order
  *op = ntohl(*op);

  memcpy(op, &header, sizeof(*op)); //copy from start of the header
  memcpy(ret, &header[sizeof(*op)], sizeof(*ret));

  //check if block data exists by checking if the second lowest bit of ret is 1. if it is, read block
  //true means the second lowest bit is 1
  if(*ret == 2){
    if (nread(fd, JBOD_BLOCK_SIZE, block) == false){ //read from block
        return false;
    }
  }

  return true;
}

/* attempts to send a packet to sd; returns true on success and false on failure */
bool send_packet(int fd, uint32_t op, uint8_t *block) {
  uint8_t packet[HEADER_LEN + JBOD_BLOCK_SIZE]; //5 and 256 bytes
  uint32_t cmd = op >> 12;
  uint8_t ret = 0;

  if (cmd == JBOD_WRITE_BLOCK){
    ret = 2; //set ret to 00000010
  }
  
  else{
    ret = 0;
  }
  //conversion
  op = htonl(op);

  memcpy(&packet, &op, sizeof(op)); //write op to packet
  memcpy(&packet[sizeof(op)], &ret, sizeof(ret));
  
  //when command is JBOD_WRITE_BLOCK, write block to packet
  if (cmd == JBOD_WRITE_BLOCK){
      memcpy(&packet[HEADER_LEN], block, JBOD_BLOCK_SIZE);
      if(nwrite(fd, HEADER_LEN + JBOD_BLOCK_SIZE, packet) == false){ //write packet to server
        return false;
      }
    }
  else{
    return nwrite(fd, HEADER_LEN, packet);
  }
  return true;
}

/* connect to server and set the global client variable to the socket */
bool jbod_connect(const char *ip, uint16_t port) {
  struct sockaddr_in caddr;
  int sock = socket(PF_INET, SOCK_STREAM, 0);

  //setup address info
  caddr.sin_family = AF_INET;
  caddr.sin_port = htons(port);

  if(sock == -1){
    printf("fail");
    return false;
  }

  inet_aton(ip, &caddr.sin_addr);
  cli_sd = sock;

  //connect socket file descriptor to the specified address
  if (connect(cli_sd, (const struct sockaddr *)&caddr, sizeof(caddr)) == -1){
    return false;
  }
  return true;
}

void jbod_disconnect(void) {
  if (cli_sd != -1) {
    close(cli_sd);
    cli_sd = -1;
  }
}

int jbod_client_operation(uint32_t op, uint8_t *block) {
  uint8_t ret = 0;

  send_packet(cli_sd, op, block);
  recv_packet(cli_sd, &op, &ret, block);
  
  return 0;
}

