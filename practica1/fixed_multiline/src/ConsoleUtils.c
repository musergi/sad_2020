#include "ConsoleUtils.h"

#include <termios.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include <stdlib.h>

static struct termios initial_settings;

static void reset()
{
    tcsetattr(STDIN_FILENO, TCSAFLUSH, &initial_settings);
}

static int get_cursor_position(int *row, int *column)
{
    char buf[30] = {0};
    int ret, i, pow;
    char ch;

    *column = 0; *row = 0;

    struct termios term, restore;

    tcgetattr(0, &term);
    tcgetattr(0, &restore);
    term.c_lflag &= ~(ICANON|ECHO);
    tcsetattr(0, TCSANOW, &term);

    write(1, "\033[6n", 4);

    for( i = 0, ch = 0; ch != 'R'; i++ )
    {
        ret = read(0, &ch, 1);
        if ( !ret )
        {
           tcsetattr(0, TCSANOW, &restore);
           return 1;
        }
        buf[i] = ch;
    }

    if (i < 2)
    {
        tcsetattr(0, TCSANOW, &restore);
        printf("i < 2\n");
        return(1);
    }

    for( i -= 2, pow = 1; buf[i] != ';'; i--, pow *= 10)
        *column = *column + ( buf[i] - '0' ) * pow;

    for( i-- , pow = 1; buf[i] != '['; i--, pow *= 10)
        *row = *row + ( buf[i] - '0' ) * pow;

    tcsetattr(0, TCSANOW, &restore);
    return 0;
}


JNIEXPORT void JNICALL Java_ConsoleUtils_init_1console(JNIEnv *env, jclass class)
{
    tcgetattr(STDIN_FILENO, &initial_settings);
    atexit(reset);
}

JNIEXPORT void JNICALL Java_ConsoleUtils_set_1raw_1mode(JNIEnv *env, jclass class)
{
    struct termios settings;
    tcgetattr(STDIN_FILENO, &settings);

    settings.c_lflag &= ~(ECHO | ICANON);

    tcsetattr(STDIN_FILENO, TCSAFLUSH, &settings);
}

JNIEXPORT jint JNICALL Java_ConsoleUtils_get_1console_1width(JNIEnv *env, jclass class)
{
    struct winsize w;
    ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
    return w.ws_col;
}

JNIEXPORT jint JNICALL Java_ConsoleUtils_get_1console_1height(JNIEnv *env, jclass class)
{
    struct winsize w;
    ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
    return w.ws_row;
}

JNIEXPORT jint JNICALL Java_ConsoleUtils_get_1cursor_1column(JNIEnv *env, jclass class)
{
    int column = 0, row = 0;
    get_cursor_position(&row, &column);
    return column;
}


JNIEXPORT jint JNICALL Java_ConsoleUtils_get_1cursor_1row(JNIEnv *env, jclass class)
{
    int column = 0, row = 0;
    get_cursor_position(&row, &column);
    return row;
}