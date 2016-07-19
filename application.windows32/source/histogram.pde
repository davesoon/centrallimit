class Histogram{
  int[] values;
  final int pnum = 10000;
  int n = 10;
  
  float mean = 0;
  float stddev = 0;
  float[][] emperical = new float[3][3];
  
  color[] hlights = {#FF4444, #44EE44, #4444FF};
  
  Histogram(){
    reset(10);
  }
  
  Histogram(int n){
    reset(n);
  }
  
  void reset(int n){
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
  
  void renderText(){
    fill(0);
    textAlign(RIGHT, TOP);
    if(n != 0){
      text("Distribution of x̄", 500, 0);
      text(String.format("for n = %1d", n), 500, 20);
    }
    else{
      text("Distribution of x", 500, 0);
    }
    text(String.format("μ  = %08f", mean), 500, 40);
    text(String.format("σ  = %08f", stddev), 500, 60);
    textSize(15);
    if(n != 0){
      text("x̄", 380, 70);
      text("x̄", 380, 50);
    }
    else{
      text("x", 380, 70);
      text("x", 380, 50);
    }
    textSize(20);
    textAlign(LEFT, TOP);
  }
  
  void render(){
    for(int i = 0; i < 100; i ++){
      if(i == mInd)
        fill(100);
      else
        fill(255);
      rect(i * 5, height - values[i]/2, 5, values[i]/2);
    }
  }
  
  void rendersec(int s){
    fill(255);
    for(int i = 0; i < 100; i ++){
      if(i >= (int)emperical[s][0])
        fill(hlights[s]);
      if(i >= (int)emperical[s][1])
        fill(255);
      rect(i * 5, height - values[i]/2, 5, values[i]/2);
    }
  }
  
  void addnum(int i){
    values[i] ++;
  }
  
  void addvalue(float f){
    int index = floor(f*100);
    if(index < 0 || index >= 100)
      return;
    addnum(index);
  }
  
  void emperical(){
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