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