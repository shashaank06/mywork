#include "buffer.h"
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

sem_t empty, full;
pthread_mutex_t buffer_mutex;
int in, out;
buffer_item my_buff[BUFFER_SIZE];
int init_buffer()
{
    in = 0;
    out = 0;
    int res = sem_init(&empty, 0, BUFFER_SIZE);
    if (res == -1)
    {
        return -1;
    }
    res = sem_init(&full, 0, 0);
    if (res == -1)
    {
        return -1;
    }
    res = pthread_mutex_init(&buffer_mutex, NULL);
    if (res == -1)
    {
        return -1;
    }
    return 0;
}

int insert_item(buffer_item item)
{
    int res = sem_wait(&empty);
    if (res == -1)
    {
        return -1;
    }
    res = pthread_mutex_lock(&buffer_mutex);
    if (res == -1)
    {
        return -1;
    }
    my_buff[in] = item;
    in = (in + 1) % BUFFER_SIZE;
    res = pthread_mutex_unlock(&buffer_mutex);
    if (res == -1)
    {
        return -1;
    }
    res = sem_post(&full);
    if (res == -1)
    {
        return -1;
    }
    return 0;
}

int remove_item(buffer_item *item)
{
    int res = sem_wait(&full);
    if (res == -1)
    {
        return -1;
    }
    res = pthread_mutex_lock(&buffer_mutex);
    if (res == -1)
    {
        return -1;
    }
    *item = my_buff[out];
    out = (out + 1) % BUFFER_SIZE;
    res = pthread_mutex_unlock(&buffer_mutex);
    if (res == -1)
    {
        return -1;
    }
    res = sem_post(&empty);
    if (res == -1)
    {
        return -1;
    }
    return 0;
}

int destroy_buffer()
{
    int res = sem_destroy(&empty);
    if (res == -1)
    {
        return -1;
    }
    res = sem_destroy(&full);
    if (res == -1)
    {
        return -1;
    }
    res = pthread_mutex_destroy(&buffer_mutex);
    if (res == -1)
    {
        return -1;
    }
    return 0;
}
