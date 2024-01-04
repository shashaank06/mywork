#include<stdio.h>
#include<stdlib.h>

int main()
{
    FILE *file;
    char *filename = "./shashaank_foo";
    /* open the file for writing */
    file = fopen(filename, "w");
    if (file == NULL)
    {
        fprintf(stderr, "File %s could not be opened\n", filename);
        exit(1);
    }
    /* write to the file */
    fprintf(file, "Hello World!\n");
    /* close the file */
    fclose(file);
}
