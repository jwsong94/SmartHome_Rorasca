#include<iostream>
#include<fstream>
#include<opencv2/opencv.hpp>
#include<time.h>
#include<string.h>

using namespace std;
using namespace cv;

int main()
{
    char filePath[1000] = "";
    time_t current = time(NULL);
    strcat(filePath, ctime(&current));
    ofstream fout;
    fout.open("/home/pi/Image/Image.txt");
    fout << filePath;
    VideoCapture capture(0);
    if(!capture.isOpened()){
        cout << "Failed to connect to the camera." << endl;
    }
    Mat frame;
    capture >> frame;
    if(frame.empty()){
        cout << "Failed to capture an image" << endl;
        return -1;
    }
    imwrite("/home/pi/Image/Image.jpg", frame);

    return 0;
}
