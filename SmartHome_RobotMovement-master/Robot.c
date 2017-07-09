#include <stdio.h>
#include <wiringPi.h>

#define A_F 21
#define A_B 22
#define B_F 23
#define B_B 24
#define C_F 25
#define C_B 26
#define D_F 27
#define D_B 28

#define PWM_PIN 1
#define M_SPEED 200

#define FF 11 // Forward
#define BB 22 // Backward
#define SS 33 // Stop

#define U 10 // Up
#define D 20 // Down
#define L 30 // Left
#define R 40 // Right
#define S 50 // Stop
#define TL 60 // Turn Left
#define TR 70 // Turn Right

#define SONAR 29 // g20 - p 40

void setState(int pos, int st){
    switch(st){
        case FF : 
            digitalWrite(pos, 1);
            digitalWrite(pos+1, 0);
            break;
        case BB :
            digitalWrite(pos, 0);
            digitalWrite(pos+1, 1);
            break;
        case SS :
            digitalWrite(pos, 0);
            digitalWrite(pos+1, 0);
            break;
        default : break;
    }
}

void setDirection(int dr){
    switch(dr){
        case U :
            setState(A_F,SS);
            setState(B_F,FF);
            setState(C_F,SS);
            setState(D_F,BB);
            printf("ABCD : SFSB\n");
            break;
        case D :
            setState(A_F,SS);
            setState(B_F,BB);
            setState(C_F,SS);
            setState(D_F,FF);
            printf("ABCD : SBSF\n");
            break;
        case L :
            setState(A_F,FF);
            setState(B_F,SS);
            setState(C_F,BB);
            setState(D_F,SS);
            printf("ABCD : FSBS\n");
            break;
        case R :
            setState(A_F,BB);
            setState(B_F,SS);
            setState(C_F,FF);
            setState(D_F,SS);
            printf("ABCD : BSFS\n");
            break;
        case S :
            setState(A_F,SS);
            setState(B_F,SS);
            setState(C_F,SS);
            setState(D_F,SS);
            printf("ABCD : SSSS\n");
            break;
        case TL :
            setState(A_F,FF);
            setState(B_F,FF);
            setState(C_F,FF);
            setState(D_F,FF);
            printf("ABCD : FFFF\n");
            break;
        case TR :
            setState(A_F,BB);
            setState(B_F,BB);
            setState(C_F,BB);
            setState(D_F,BB);
            printf("ABCD : BBBB\n");
            break;
defualt : break;
    }
}

int main(void){

    int i = 0;
    int distance = 0;

    printf("Main Start\n");

    if(wiringPiSetup() == -1)
        return 1;

    printf("WiringPi Setup END\n");

    pinMode (PWM_PIN, PWM_OUTPUT);
    pinMode (A_F, OUTPUT);
    pinMode (A_B, OUTPUT);
    pinMode (B_F, OUTPUT);
    pinMode (B_B, OUTPUT);
    pinMode (C_F, OUTPUT);
    pinMode (C_B, OUTPUT);
    pinMode (D_F, OUTPUT);
    pinMode (D_B, OUTPUT);

    pwmWrite(PWM_PIN, M_SPEED);

    printf("Pin Mode Setting End\n");
    setDirection(S);

    delay(2000);
    while(1){
        char c = getchar();
        switch(c){
        case 'u' : setDirection(U); break;
        case 'd' : setDirection(D); break;
        case 'l' : setDirection(L); break;
        case 'r' : setDirection(R); break;
        case 's' : setDirection(S); break;
        }
    }

    return 0;
}

