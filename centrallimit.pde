/*
David Sun 2016
Central Limit Demo
*/

import java.util.Random;

Random rand;
Histogram hist;
int mInd = 0;

int intmode = -1;

void settings(){
  size(500, 400);
}

void setup(){
  surface.setTitle("Central Limit Theorem");
  rand = new Random();
  hist = new Histogram(1);
  textSize(20);
  textAlign(LEFT, TOP);
  noStroke();
  //println(hist.emperical[1][1]);
}

void draw(){
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

void keyPressed(){
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

float xbar(int n){
  float s = 0;
  for(int i = 0; i < n; i ++){
    s += rand.nextFloat();
  }
  return s/n;
}

float perfectx(int seed){
  return (float)(seed % 100) / 100 + .005;
}

float perfectxbar(int seed, int n){
  float sum = 0;
  for(int i = seed; i < seed + n; i ++){
    sum += perfectx(i);
  }
  return sum/n;
}

void renderstat(float x, float y){
  mInd = round((float)mouseX / width * 100);
  if(mInd == 100)
    mInd --;
  fill(0);
  text(String.format("%05d/100000", hist.values[mInd]), x, y);
  text(String.format("%.2f < x̄ ≤ %.2f", (float)mInd/100, (float)(mInd+1)/100), x, y + 20);
  //text((float)mInd/100 + " < x̄ ≤ " + (float)(mInd + 1)/100, x, y + 20);
  //text(mouseX, mouseX, mouseY);
}

void renderstatsec(){
  fill(0);
  text(String.format("%.2f", hist.emperical[intmode][2]*100) + "%", 0, 0);
  text(String.format("%.2f < x̄ < %.2f", hist.emperical[intmode][0]/100, hist.emperical[intmode][1]/100), 0, 20);
  text(String.format("-%d < z < %d", intmode + 1, intmode + 1), 2, 41);
  fill(hist.hlights[intmode]);
  text(String.format("-%d < z < %d", intmode + 1, intmode + 1), 0, 40);
  //text((float)mInd/100 + " < x̄ ≤ " + (float)(mInd + 1)/100, 0, 20);
}