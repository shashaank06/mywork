#ifndef BUFFER_H
#define BUFFER_H

#include <pthread.h>
#include <semaphore.h>

#define BUFFER_SIZE 5
typedef char *buffer_item;

int init_buffer();

int insert_item(buffer_item item);

int remove_item(buffer_item *item);

int destroy_buffer();

#endif
