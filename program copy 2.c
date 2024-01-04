#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

static int INPUT_SIZE = 10;
static char OUTPUT_OF_FILE[] = "sorted.txt";

void sorting(int a[]);
void write_to_file(int a[]);

int main()
{
    int input[INPUT_SIZE];
    printf("Enter %d numbers to sort: \n", INPUT_SIZE);

    for (int i = 0; i < INPUT_SIZE; i++)
    {
        scanf("%d", &input[i]);
    }
    
    sorting(input);
    printf("Numbers after sorting: \n");

    for (int i = 0; i < INPUT_SIZE; i++)
    {
        printf("%d\n", input[i]);
    }

    write_to_file(input);
    return 0;
}

void sorting(int a[])
{
    // bubble sort
    for (int i = 0; i < INPUT_SIZE; i++)
    {
        for (int j = 0; j < INPUT_SIZE - 1; j++)
        {
            if (a[j] < a[j + 1])
            {
                int tv = a[j];
                a[j] = a[j + 1];
                a[j + 1] = tv;
            }
        }
    }
}

void write_to_file(int a[])
{
    int file_permissions = 0644;
    int file_open_flags = O_WRONLY | O_CREAT | O_TRUNC;
    int file = open(OUTPUT_OF_FILE, file_open_flags, file_permissions);

    if (file == -1)
    {
        fprintf(stderr, "Error opening file! Unable to create/open the file %s \n", OUTPUT_OF_FILE);
        return;
    }

    char buffer_v[4 * 1024 * 1024]; 
    for (int i = 0; i < INPUT_SIZE; i++)
    {
        int len = sprintf(buffer_v, "%d", a[i]);
        if (i != INPUT_SIZE - 1)
        {
            len += sprintf(buffer_v + len, "\n");
        }
        write(file, buffer_v, len);
    }
    close(file);
}
