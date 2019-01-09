#include <jni.h>
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <iostream>
#include <stdio.h>
using namespace std;
using namespace cv;
CascadeClassifier face_cascade;
extern "C"
{
jint JNICALL Java_com_example_yohan_testcv_MainActivity_ctranslate(JNIEnv *env, jobject instance,
                                                                   jlong matAddress,jstring path,jlong faceAddress)
{
    char const * cpath;
    Mat &face = *(Mat *) faceAddress;
    cpath = env->GetStringUTFChars(  path , NULL ) ;
    Mat &frame = *(Mat *) matAddress;
    face_cascade.load((cpath));
    if( !face_cascade.load( cpath ) )
    {
        printf("--(!)Error loading face cascade\n");
        Point p1(0,0);
        Point p2(200,200);
        rectangle(frame, p1, p2, Scalar(255, 0, 255), 4, 8, 0);
    }
    else
    {
        std::vector<Rect> faces;
        Mat frame_gray;
        cvtColor(frame, frame_gray, COLOR_BGR2GRAY);
        equalizeHist(frame_gray, frame_gray);
        int maxsize=0;
        face_cascade.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE,
                                      Size(30, 30));
        if(faces.size()==0)
            return 0;
        for (size_t i = 0; i < faces.size(); i++)
        {
            Point p1(faces[i].x, faces[i].y);
            Point p2(faces[i].x + faces[i].width, faces[i].y + faces[i].height);
            rectangle(frame, p1, p2, Scalar(255, 0, 255), 4, 8, 0);
            if(maxsize<(faces[i].width*faces[i].height))
            {
                cv::resize(face, face, Size(faces[i].height,faces[i].width), 1, 1, cv::INTER_CUBIC);
                face = frame(faces[i]);

            }


        }

    }
    return 1;
}



}