#include <stdio.h>

static int INPUT_SIZE = 10;
static char OUTPUT_OF_FILE[] = "sorted.txt";

void sorting(int a[]);
void write_to_file(int a[]);
int main()
{
    int input[INPUT_SIZE];
    printf("Please Enter %d numbers to sort: \n", INPUT_SIZE);
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
    // sorting
    for (int i = 0; i < INPUT_SIZE; i++)
    {
        for (int j = 0; j < INPUT_SIZE - 1; j++)
        {
            if (a[j] > a[j + 1])
            {
                int t_v = a[j];
                a[j] = a[j + 1];
                a[j + 1] = t_v;
            }
        }
    }
}

void write_to_file(int a[])
{
    FILE *out_file = fopen(OUTPUT_OF_FILE, "w");
    for (int i = 0; i < INPUT_SIZE; i++)
    {
        if (i == INPUT_SIZE - 1)
        {
            fprintf(out_file, "%d", a[i]);
        }
        else
        {
            fprintf(out_file, "%d\n", a[i]);
        }
    }
    fclose(out_file);
}
