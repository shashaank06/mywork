#include <stdio.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>


#define HISTORY_SIZE 50

char error_message[] = "An error has occurred (from SRG)\n";

struct mynod {
    char *myval;
    struct mynod *mylink;
};
struct TshContext {
    bool my_execvp;
    struct myllist *history;
    char *path;
};

struct myllist {
    struct mynod *myhd;
    int full_count;
};

struct myllist *init_list(int full_count) {
    struct myllist *mylst = malloc(sizeof(struct myllist));
    mylst->myhd = NULL;
    mylst->full_count = full_count;
    return mylst;
}

void destroy_list(struct myllist *mylst) {
    struct mynod *mycur = mylst->myhd;
    while (mycur!= NULL) {
        struct mynod *mylink = mycur->mylink;
        free(mycur);
        mycur = mylink;
    }
    free(mylst);
}

void q_push(struct myllist *mylst,char *myval){
	struct mynod *new_node= malloc(sizeof(struct mynod));
    new_node->myval = myval;
    if (mylst->myhd == NULL) {
        new_node->mylink = mylst -> myhd;
        mylst->myhd = new_node;
        return;
    } else {
    	struct mynod *n1 = mylst->myhd;  
    while(n1-> mylink != NULL){
    	n1 = n1->mylink;
    }
        new_node -> mylink = NULL;
        new_node->myval = myval;
        n1->mylink = new_node;
        
        return;
    }
}

void q_pop_if_oflow(struct myllist *mylst) {
    if (mylst->full_count == 0) {
        return;
    }
    if (mylst->full_count == 1) {
        free(mylst->myhd->myval);
        free(mylst->myhd);
        mylst->myhd = NULL;
        return;
    }
    
    struct mynod *mycur = mylst->myhd;
    struct mynod *myprev = NULL;
    int current_size = 0;
    while (mycur->mylink != NULL) {
        myprev = mycur;
        mycur = mycur->mylink;
        current_size++;
    }
    if (current_size <= mylst->full_count) {
        return;
    }
    free(mycur->myval);
    free(mycur);
    myprev->mylink = NULL;
}

char *next_token(char **input) {
    char *token;
    while ((token = strsep(input, " \t\n")) != NULL) {
        if (!isspace((unsigned char) *token) && *token != '\n' && *token != '\t' &&
            *token != '\0' && *token != ' ') {
            return token;
        }
    }
    return NULL;
}

int count_tokens(char *input) {
    int count = 0;
    char *dup = strdup(input);
    char *token;
    while ((token = strsep(&dup, " \t\n")) != NULL) {
        if (!isspace((unsigned char) *token) && *token != '\n' && *token != '\t' &&
            *token != '\0' && *token != ' ') {
            count++;
        }
    }
    free(dup);
    return count;
}

char *lookup(char *command, char *path) {
    if (command == NULL || path == NULL || strlen(command) == 0 ||
        strlen(path) == 0) {
        return NULL;
    }
    if (command[0] == '/' || command[0] == '.') {
        return strdup(command);
    }
    char *path_dup = strdup(path);
    char *dup = path_dup;
    char *token;
    char *res = NULL;
    while ((token = strsep(&dup, ":")) != NULL) {
        char *comp_path =
            malloc(sizeof(char) * (strlen(token) + strlen(command) + 2));
        strcpy(comp_path, token);
        strcat(comp_path, "/");
        strcat(comp_path, command);
        if (access(comp_path, X_OK) == 0) {
            res = comp_path;
            break;
        }
        free(comp_path);
    }
    free(path_dup);
    return res;
}

void run_command(char *command, char *line, struct TshContext *context) {
    int total_tokens = count_tokens(line);
    if (total_tokens < 5) {
        total_tokens = 5;
    }
    total_tokens += 2;
    char **myarg = malloc(sizeof(char *) * total_tokens);
    myarg[0] = lookup(command, context->path);
    int i = 1;
    int old_stdin = dup(STDIN_FILENO);
    while (i < total_tokens && line != NULL) {
        myarg[i] = next_token(&line);
        if (myarg[i] != NULL && strcmp("<", myarg[i]) == 0) {
            myarg[i] = next_token(&line);
            if (myarg[i] == NULL) {
                write(STDERR_FILENO, error_message, strlen(error_message));
                return;
            }
            int in_fd = open(myarg[i], O_RDONLY);
            if (in_fd < 0) {
                write(STDERR_FILENO, error_message, strlen(error_message));
                exit(0);
            }
            dup2(in_fd, 0);
            close(in_fd);
            myarg[i] = NULL;
        }
        i++;
    }
    if (myarg[0] == NULL) {
        write(STDERR_FILENO, error_message, strlen(error_message));
    } else {
        pid_t p_id = fork();
        if (p_id < 0) {
            write(STDERR_FILENO, error_message, strlen(error_message));
        } else if (p_id == 0) {
            if (!context->my_execvp) {
                int ret =
                    execlp(command, myarg[0], myarg[1], myarg[2], myarg[3], myarg[4], NULL);
                if (ret == -1) {
                    write(STDERR_FILENO, error_message, strlen(error_message));
                }
            } else {
                int ret = execvp(command, myarg);
                if (ret == -1) {
                    write(STDERR_FILENO, error_message, strlen(error_message));
                }
            }
        } else {
            int waiting;
            int waiting_return = wait(&waiting);
            if (waiting_return == -1) {
                write(STDERR_FILENO, error_message, strlen(error_message));
            }
            free(myarg[0]);
            free(myarg);
            dup2(old_stdin, STDIN_FILENO);
            close(old_stdin);
            }
        
    }
}

void run_cd(char *line) {
    int token_length = count_tokens(line);
    if (token_length != 1) {
        write(STDERR_FILENO, error_message, strlen(error_message));
        return;
    }
    char *dir = next_token(&line);
    int ret = chdir(dir);
    if (ret == -1) {
        write(STDERR_FILENO, error_message, strlen(error_message));
    }
}

void run_path(char *line, struct TshContext *context) {
    int token_length = count_tokens(line);
    if (token_length == 0) {
        if (context->path == NULL) {
           printf("Path is set to: \n");
            return;
        }
        printf("path is set to %s\n", context->path);
        return;
    }
    free(context->path);
    context->path = strdup(next_token(&line));
}

void run_history(struct TshContext *context, char *line) {
    int token_length = count_tokens(line);
    int n = HISTORY_SIZE;
    if (token_length == 1) {
        n = atoi(next_token(&line));
    } else if (token_length > 1) {
        write(STDERR_FILENO, error_message, strlen(error_message));
        return;
    }
    struct mynod *mycur = context->history->myhd;
    int temp =1;
    while (n > 0 && mycur != NULL) {
        printf("%d:%s\n",temp++,mycur->myval);
        mycur = mycur->mylink;
        n--;
    }
}

struct TshContext *init_context(bool my_execvp) {
    struct TshContext *context = malloc(sizeof(struct TshContext));
    context->my_execvp = my_execvp;
    context->path = NULL;
    context->history = init_list(HISTORY_SIZE);
    return context;
}

void destroy_context(struct TshContext *context) {
    if (context->path != NULL) {
        free(context->path);
    }
    if (context->history != NULL) {
        destroy_list(context->history);
    }
    free(context);
}

int main(int argl, char *arguments[]) {
    bool my_execvp = true;
    bool my_execlp=true;
    if (argl > 1) {
        if (strcmp(arguments[1], "-execlp") == 0) {
            my_execvp = false;
            my_execlp=true;
               printf("**Based on your choice, execlp() will be used**\n");
        }
        else if (strcmp(arguments[1], "-execvp")==0) {
        	my_execvp= true;
        	my_execlp=false;
            printf("**Based on your choice, execvp() will be used**\n");
        } else {
            printf("Invalid argument\n");
            exit(0);
        }
    }else{
       printf("**By default execvp() will be used**\n");
       }
    
    size_t len = 0;
    char *line = NULL;
    ssize_t read;
    struct TshContext *context = init_context(my_execvp);
    while (true) {
    
        printf("tsh> ");
        read = getline(&line, &len, stdin);
        if (read == -1) {
            write(STDERR_FILENO, error_message, strlen(error_message));
        }
        char *complete_line = strdup(line);
        char *command = next_token(&line);
        if (strcmp("exit", command) == 0) {
            destroy_context(context);
            exit(0);
        } else if (strcmp("path", command) == 0) {
            run_path(line, context);
        } else if (strcmp("cd", command) == 0) {
            run_cd(line);
        } else if (strcmp("history", command) == 0) {
            run_history(context, line);
        } else {
            run_command(command, line, context);
        }
        q_push(context->history, complete_line);
        q_pop_if_oflow(context->history);
    }
}

