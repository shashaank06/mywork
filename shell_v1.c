#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

typedef struct {
  int argl;
  char **arguments;
} Args;

int main(int argl, char *arguments[]) {
  if (argl< 2) {
    printf("More arguments required\n");
    return 0;
  }

  Args args;
  args.argl = argl;
  args.arguments = arguments;

  pid_t pid = fork();

  if (pid < 0) {
    perror("Problem in executing fork");
    return 1;
  } else if (pid == 0) {
    bool my_execlp = false;
    int arg_start = 1;
    if (args.argl >= 2) {
      if (strcmp(args.arguments[arg_start], "-execlp") == 0) {
        my_execlp = true;
        arg_start++;
      } else if (strcmp(args.arguments[arg_start], "-execvp") == 0) {
        my_execlp = false;
        arg_start++;
      }
    }
    char *command = NULL;
    if (arg_start < args.argl) {
      command = args.arguments[arg_start];
    }

    if (my_execlp) {
      execlp(command, command, args.arguments[arg_start + 1], args.arguments[arg_start + 2], args.arguments[arg_start + 3], args.arguments[arg_start + 4], NULL);
      perror("execlp not success ");
    } else {
      execvp(command, &args.arguments[arg_start]);
      perror("execvp not success");
      return 1;
    }
  } else {
    int waiting;
    int wait_return=wait(&waiting);
    if(wait_return == -1){
    perror("Wait Failed");
    return 1;
    }
    printf("-- the end of shell_v1 --\n");
  }
  return 0;
}
