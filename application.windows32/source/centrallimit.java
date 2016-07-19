import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class centrallimit extends PApplet {



Random rand;
Histogram hist;
int mInd = 0;

int intmode = -1;

public void settings(){
  size(500, 400);
}

public void setup(){
  surface.setTitle("Central Limit Theorem");
  rand = new Random();
  hist = new Histogram(1);
  textSize(20);
  textAlign(LEFT, TOP);
  noStroke();
  //println(hist.emperical[1][1]);
}

public void draw(){
  background(200);
  if(intmode != -1){
    hist.rendersec(intmode);
    renderstatsec();
  }
  else{
    hist.render();
    renderstat(0, 0);
  }
  hist.renderText();
  //text(frameRate, 0, 100);
}

public void keyPressed(){
  if(keyCode == DOWN){
    if(hist.n > 0)
      hist.reset(hist.n - 1);
  }
  if(keyCode == UP){
    hist.reset(hist.n + 1);
  }
  
  if(key == '1'){
    intmode = 0;
  }
  else if(key == '2'){
    intmode = 1;
  }
  else if(key == '3'){
    intmode = 2;
  }
  else if(key == '0' || key == '`'){
    intmode = -1;
  }
}

public float xbar(int n){
  float s = 0;
  for(int i = 0; i < n; i ++){
    s += rand.nextFloat();
  }
  return s/n;
}

public float perfectx(int seed){
  return (float)(seed % 100) / 100 + .005f;
}

public float perfectxbar(int seed, int n){
  float sum = 0;
  for(int i = seed; i < seed + n; i ++){
    sum += perfectx(i);
  }
  return sum/n;
}

public void renderstat(float x, float y){
  mInd = round((float)mouseX / width * 100);
  if(mInd == 100)
    mInd --;
  fill(0);
  text(String.format("%05d/100000", hist.values[mInd]), x, y);
  text(String.format("%.2f < x\u0304 \u2264 %.2f", (float)mInd/100, (float)(mInd+1)/100), x, y + 20);
  //text((float)mInd/100 + " < x\u0304 \u2264 " + (float)(mInd + 1)/100, x, y + 20);
  //text(mouseX, mouseX, mouseY);
}

public void renderstatsec(){
  fill(0);
  text(String.format("%.2f", hist.emperical[intmode][2]*100) + "%", 0, 0);
  text(String.format("%.2f < x\u0304 < %.2f", hist.emperical[intmode][0]/100, hist.emperical[intmode][1]/100), 0, 20);
  text(String.format("-%d < z < %d", intmode + 1, intmode + 1), 2, 41);
  fill(hist.hlights[intmode]);
  text(String.format("-%d < z < %d", intmode + 1, intmode + 1), 0, 40);
  //text((float)mInd/100 + " < x\u0304 \u2264 " + (float)(mInd + 1)/100, 0, 20);
}
class Histogram{
  int[] values;
  final int pnum = 10000;
  int n = 10;
  
  float mean = 0;
  float stddev = 0;
  float[][] emperical = new float[3][3];
  
  int[] hlights = {0xffFF4444, 0xff44EE44, 0xff4444FF};
  
  Histogram(){
    reset(10);
  }
  
  Histogram(int n){
    reset(n);
  }
  
  public void reset(int n){
    this.n = n;
    values = new int[100];
    mean = 0;
    
    //long starttime = System.currentTimeMillis();
    ArrayList<Float> datapoints = new ArrayList<Float>();
    for(int i = 0; i < pnum; i ++){
      float dpoint;
      if(n == 0)
        dpoint = perfectx(i);
      else
        dpoint = xbar(n);
      addvalue(dpoint);
      mean += dpoint/pnum;
      datapoints.add(dpoint);
    }
    
    stddev = 0;
    for(Float f : datapoints){
      stddev += (f - mean)*(f - mean)/(pnum);
    }
    stddev = sqrt(stddev);
    emperical();
    //long endtime = System.currentTimeMillis();
    //println(endtime - starttime);
  }
  
  public void renderText(){
    fill(0);
    textAlign(RIGHT, TOP);
    if(n != 0){
      text("Distribution of x\u0304", 500, 0);
      text(String.format("for n = %1d", n), 500, 20);
    }
    else{
      text("Distribution of x", 500, 0);
    }
    text(String.format("\u03bc  = %08f", mean), 500, 40);
    text(String.format("\u03c3  = %08f", stddev), 500, 60);
    textSize(15);
    if(n != 0){
      text("x\u0304", 380, 70);
      text("x\u0304", 380, 50);
    }
    else{
      text("x", 380, 70);
      text("x", 380, 50);
    }
    textSize(20);
    textAlign(LEFT, TOP);
  }
  
  public void render(){
    for(int i = 0; i < 100; i ++){
      if(i == mInd)
        fill(100);
      else
        fill(255);
      rect(i * 5, height - values[i]/2, 5, values[i]/2);
    }
  }
  
  public void rendersec(int s){
    fill(255);
    for(int i = 0; i < 100; i ++){
      if(i >= (int)emperical[s][0])
        fill(hlights[s]);
      if(i >= (int)emperical[s][1])
        fill(255);
      rect(i * 5, height - values[i]/2, 5, values[i]/2);
    }
  }
  
  public void addnum(int i){
    values[i] ++;
  }
  
  public void addvalue(float f){
    int index = floor(f*100);
    if(index < 0 || index >= 100)
      return;
    addnum(index);
  }
  
  public void emperical(){
    for(int b = 0; b < 3; b ++){
      float sum = 0;
      for(int a = floor((mean - (b+1)*stddev) * 100); a < floor((mean + (b+1)*stddev) * 100); a ++){
        if(a >= 0 && a < 100)
          sum += (float)values[a]/pnum;
      }
      emperical[b][0] = floor((mean - (b+1)*stddev) * 100);
      if(emperical[b][0] < 0)
        emperical[b][0] = 0;
      emperical[b][1] = floor((mean + (b+1)*stddev) * 100);
      if(emperical[b][1] > 100)
        emperical[b][1] = 100;
      emperical[b][2] = sum;
      //println(sum);
    }
    //println();
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "centrallimit" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
