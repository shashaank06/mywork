#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  pid_t cid;
  cid = fork();

  if (cid < 0) {
    printf("A fork error has occured. \n");
    exit(-1);
  } else if (cid == 0) /* we are in the child. */
  {
    int New = 0;
    printf("I am the child about to call ps using execlp. \n");
    New = execlp("/bin/ls", "ls", (char *)0);
    if (New == -1) {
      /* If execlp() is successful, we should not reach this next line. */
      printf("The call to execlp() was not successful. \n");
      exit(127);
    }
  } else { /*we are in the parent */
    int c_pid = wait(0);
    printf("I am the parent. The child just ended. I will now exit .\n");
    if (c_pid == -1) {
      printf("The expected child process is not created %d", c_pid);

    } else if (c_pid > 0) {
      printf("The child process id value is %d \n", c_pid);
    }
    exit(0);
  }
  return (0);
}
