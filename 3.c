#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>

#define BUFFER_SIZE 25
#define READ_END 0
#define WRITE_END 1

int main(void) {
  char write_msg[BUFFER_SIZE] = "Greetings";
  char read_msg[BUFFER_SIZE];
  int fd[2];
  pid_t pid;

  if (pipe(fd) == -1) {
    fprintf(stderr, "Pipe failed");
    return 1;
  }
  printf(" read descriptor is %d \n", fd[0]);
  printf(" write descriptor is %d \n", fd[1]);
  /* fork a child process */
  pid = fork();

  if (pid < 0) { /* error occurred */
    fprintf(stderr, "Fork Failed");
    return 1;
  }

  if (pid > 0) { /* parent process */
    /* close the unused end of the pipe */
    close(fd[READ_END]);

    /* write to the pipe */
    int wr = 0;
    wr = write(fd[WRITE_END], write_msg, strlen(write_msg) + 1);
    if (wr == -1) {
      printf("%d some errorin writing \n", wr);
    }
    if (wr > 0) {
      printf(" %d Data is written\n", wr);
    }
    int cl = 0;
    /* close the write end of the pipe */
    cl = close(fd[WRITE_END]);
    if (cl == -1) {
      printf(" %d close failed \n", cl);
    }
    if (cl == 0) {
      printf(" %d close completed \n", cl);
    }
  } else { /* child process */
    /* close the unused end of the pipe */
    close(fd[WRITE_END]);

    /* read from the pipe */
    read(fd[READ_END], read_msg, BUFFER_SIZE);
    printf(" read %s \n", read_msg);

    /* close the read end of the pipe */
    close(fd[READ_END]);
  }

  return 0;
}

