#include "buffer.h"
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#define MY_INITIAL_CAPACITY 10
#define WORD_DELIMITER '\n'
#define MY_WORD_DICT "./wordsEn.txt"

char **my_words;
int num_words;

void read_my_words()
{
    FILE *my_file = fopen(MY_WORD_DICT, "r");

    if (my_file == NULL)
    {
        perror("Error opening file");
        exit(EXIT_FAILURE);
    }

    num_words = 0;

    int capacity = MY_INITIAL_CAPACITY;
    my_words = (char **)malloc(capacity * sizeof(char *));

    if (my_words == NULL)
    {
        perror("Memory allocation error");
        exit(EXIT_FAILURE);
    }

    char my_buff[256];

    while (fgets(my_buff, sizeof(my_buff), my_file) != NULL)
    {
        size_t len = strlen(my_buff);
        if (len > 0 && my_buff[len - 1] == '\n')
        {
            my_buff[len - 1] = '\0';
            len--;
        }

        if (num_words == capacity)
        {
            capacity *= 2;
            my_words = (char **)realloc(my_words, capacity * sizeof(char *));
            if (my_words == NULL)
            {
                perror("Memory allocation error occured");
                exit(EXIT_FAILURE);
            }
        }

        my_words[num_words] = (char *)malloc((len + 1) * sizeof(char));
        if (my_words[num_words] == NULL)
        {
            perror("Memory allocation error occured");
            exit(EXIT_FAILURE);
        }
        strcpy(my_words[num_words], my_buff);
        num_words++;
    }

    fclose(my_file);
}

void free_words()
{
    for (int i = 0; i < num_words; i++)
    {
        free(my_words[i]);
    }
    free(my_words);
}
void *producer(void *arg)
{

    while (1)
    {
        int my_slp_time = rand() % 6;
        sleep(my_slp_time);
        buffer_item next = my_words[rand() % num_words];
        if (insert_item(next) != -1)
        {
            printf("[producer thread ID: %ld] inserted an item (word: %s) to the buffer\n", pthread_self(), next);
        }
        else
        {
            perror("Error: encounterd error while inserting an buffer item\n");
            pthread_exit(NULL);
        }
    }
    pthread_exit(NULL);
}

void *consumer(void *arg)
{
    while (1)
    {
        int my_slp_time = rand() % 6;
        sleep(my_slp_time);
        buffer_item removed;
        if (remove_item(&removed) != -1)
        {
            printf("[consumer thread ID: %ld] removed an item (word: %s) from the buffer\n", pthread_self(), removed);
        }
        else
        {
            perror("Error: encounterd error while removing an buffer item\n");
            pthread_exit(NULL);
        }
    }

    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    int s_btw = atoi(argv[1]);
    int p_cnt = atoi(argv[2]);
    int c_cnt = atoi(argv[3]);
    read_my_words();
    int my_res = init_buffer();
    if (my_res == -1)
    {
        perror("Unable to init buffer\n");
        exit(EXIT_FAILURE);
    }
    pthread_t producer_threads[p_cnt];
    pthread_t consumer_threads[c_cnt];

    for (int i = 0; i < p_cnt; i++)
    {
        int my_res = pthread_create(&producer_threads[i], NULL, producer, NULL);
        if (my_res == -1)
        {
            perror("Unable to create new thread\n");
            exit(EXIT_FAILURE);
        }
    }
    for (int i = 0; i < c_cnt; i++)
    {
        int my_res = pthread_create(&consumer_threads[i], NULL, consumer, NULL);
        if (my_res == -1)
        {
            perror("Unable to create new thread\n");
            exit(EXIT_FAILURE);
        }
    }
    sleep(s_btw);
    destroy_buffer();
    free_words();
    return 0;
}
