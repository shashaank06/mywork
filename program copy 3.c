#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

void count_lines_words(FILE *file, long long int *lc, long long int *wc);
void process_the_file(char *file_name);

 int main()
{
    printf("Enter file name or absolute path to count words and lines: ");
    char file_name[256];
    scanf("%s", file_name);
    process_the_file(file_name);
    return 0;
}
void count_lines_words(FILE *file, long long int *lc, long long int *wc)
{
    char line[4 * 1024 * 1024];
    char dl[] = " \t\n";
    char *tkn;
    
    while (fgets(line, sizeof(line), file))
    {
        int len = strlen(line);
        if (len > 0 && line[len - 1] == '\n')
        {
            (*lc)++;
        }

        // Word count using Tokens
        tkn = strtok(line, dl);
        while (tkn != NULL)
        {
            (*wc)++;
            tkn = strtok(NULL, dl);
        }
    }
}

void process_the_file(char *file_name)
{
    FILE *file = fopen(file_name, "r");

    if (file == NULL)
    {
        perror("Can't to open the file");
        return;
    }

    long long int lc_i = 0;
    long long int wc_i = 0;
    count_lines_words(file, &lc_i, &wc_i);
    printf("Lines: %lld\n", lc_i);
    printf("Words: %lld\n", wc_i);
    fclose(file);
}


