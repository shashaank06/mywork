#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <pthread.h>

#define my_bufsize 5
#define my_max_prodlimit 1000000

struct ThreadContext
{
    int *my_buf;
    int *my_cnt;
    int my_idx;
};

void *p_thrd(void *arg)
{
    struct ThreadContext *p_ctx = (struct ThreadContext *)arg;
    for (int i = 1; i <= my_max_prodlimit; i++)
    {
        while (*(p_ctx->my_cnt) == my_bufsize )
        {
          
        }
        int idx = p_ctx->my_idx;
        p_ctx->my_buf[idx] = i;
        p_ctx->my_idx = (idx + 1) % my_bufsize;
        (*p_ctx->my_cnt)++;
    }
    pthread_exit(NULL);
}

void *c_thrd(void *arg)
{
    struct ThreadContext *c_ctx = (struct ThreadContext *)arg;
    int prv = 0;
    while (true)
    {
        while (*(c_ctx->my_cnt) == 0)
        {
           
        }
        int idx = c_ctx->my_idx;
        int cnsmd = c_ctx->my_buf[idx];
        c_ctx->my_idx = (idx + 1) % my_bufsize;
        (*c_ctx->my_cnt)--;
        if (cnsmd - prv != 1)
        {
            printf("ERROR: The consumer received the sequence up to %d correctly. The next number (incorrect) number was %d.\n", prv, cnsmd);
            pthread_exit(NULL);
        }
        if (cnsmd == my_max_prodlimit)
        {
            printf("Consumer received the sequence correctly till the limit");
            pthread_exit(NULL);
        }
        prv = cnsmd;
    }
}

int main()
{

    int *my_buf = (int *)malloc(my_bufsize * sizeof(int));

    if (my_buf == NULL)
    {
        fprintf(stderr, "Memory allocation failed\n");
        return 1;
    }

    int my_buf_cnt = 0;
    struct ThreadContext p_cntxt = {my_cnt : &my_buf_cnt, my_idx : 0, my_buf : my_buf};
    struct ThreadContext c_cntxt = {my_cnt : &my_buf_cnt, my_idx : 0, my_buf : my_buf};
    pthread_t my_prod, my_consu;
    if (pthread_create(&my_prod, NULL, p_thrd, (void *)&p_cntxt) != 0)
    {
        printf("error in creating producer thread");
        return 1;
    }
    if (pthread_create(&my_consu, NULL, c_thrd, (void *)&c_cntxt) != 0)
    {
    
        printf("error in creating consumer thread");
        return 1;
    }
    pthread_join(my_consu, NULL);
    return 0;
}
