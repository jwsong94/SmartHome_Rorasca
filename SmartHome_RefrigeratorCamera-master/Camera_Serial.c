#include <stdio.h>
#include <string.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>

#include <wiringPi.h>
#include <wiringSerial.h>

int fd;
char device[] = "/dev/ttyACM0";
int baud = 9600;

time_t current;

int setup(){    
    printf("Arduino LOAING...\n");
    if((fd = serialOpen(device, baud)) < 0){
        printf("Arduino FAIL!!\n");
        return -1;
    }
    printf("Arduino COMPLETE\n");
    return 0;
}

void loop(){
    char input='N';
    
    // Receive
    if(serialDataAvail(fd)){
        //printf("%c", serialGetchar(fd));
        input = serialGetchar(fd);
        printf("%c\n", input);
        // serial character store in array
    }
    if(input == 'M'){
        printf("***************\n");
        printf("*Capture Image*\n");
        printf("***************\n");
        system("rm /home/pi/Image/*");
        pid_t pid = fork();
        int status;
        if(pid==0)
            execl("./Capture", 0);
        wait(&status);
    }

    serialFlush(fd);
}
int main(){
    if(setup()==-1)
        return 1;

    while(1){
        loop();
    }

    return 0;
}
