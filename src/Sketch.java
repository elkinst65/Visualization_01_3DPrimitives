import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Sketch extends PApplet {

  float posScale = 0.01f;
  float camDist = 3000f;
  float camAngle = 45.0f;

  Player CAP1;
  Player CAP2;
  Player DRP10A;
  Player DRP12A;
  Player DRP14B;
  Player DRP15;
  Player DRP06;
  

  
  float gameTimer = 0;
  int thisTime = 0;
  int lastTime = 0;
  float timeScale = 60;
  PVector pos;
  int history = 150;
  int firstPt = 0;
  int radarFirst = 0;
  int radarLast = 0;
  Track thisTrack;


public void  settings() {
  size(1000,1000,P3D);
}

public void setup() {
    background(0);
    CAP1 = new Player("CAP1.csv",posScale, true, true, 0, 255, 0);
    CAP2 = new Player("CAP2.csv",posScale, true, true, 0, 255, 255);
    DRP10A = new Player("DRP10A.csv",posScale, false, true, 255, 0, 0);
    DRP12A = new Player("DRP12A.csv",posScale, false, true, 255, 255, 0);
    DRP14B = new Player("DRP14B.csv",posScale, false, true, 255,128,0);
    DRP15 = new Player("DRP15.csv",posScale,false,true,255,0,255);
    DRP06 = new Player("DRP06.csv",posScale,false,true,0,0,255);
    
    gameTimer = CAP1.offset;
    if (CAP2.offset < gameTimer) {gameTimer = CAP2.offset;}
}

public void mouseWheel(MouseEvent mEvent) {
  camDist += 100 * mEvent.getCount();
}

public void mouseDragged(){
  //camAngle += (mouseY - (height/2.0)) / height * 90.0;
  camAngle = map(mouseY, 0, height, 90,0);
}

public void keyPressed() {
  //println("Key:", keyCode);
  if (keyCode == 139) {
    timeScale += 5;
    println(timeScale);
  }
  if (keyCode == 140) {
    timeScale -= 5;
    println(timeScale);
  }
  if (keyCode == 128){
    DRP10A.show();
    DRP12A.show();
    DRP14B.show();
    DRP15.show();
    DRP06.show();
  }
  if (keyCode == 129) {
    DRP10A.show();
    DRP12A.hide();
    DRP14B.hide();
    DRP15.hide();
    DRP06.hide();
  }
  if (keyCode == 130) {
    DRP10A.hide();
    DRP12A.show();
    DRP14B.hide();
    DRP15.hide();
    DRP06.hide();
  }
  if (keyCode == 131) {
    DRP10A.hide();
    DRP12A.hide();
    DRP14B.show();
    DRP15.hide();
    DRP06.hide();
  }
  if (keyCode == 132) {
    DRP10A.hide();
    DRP12A.hide();
    DRP14B.hide();
    DRP15.show();
    DRP06.hide();
  }
  if (keyCode == 133) {
    DRP10A.hide();
    DRP12A.hide();
    DRP14B.hide();
    DRP15.hide();
    DRP06.show();
  }
}

public void draw() {
     background(0,0,0);

     //  camera(width/2.0,height/2.0,(height/2.0)/tan(PI*30.0/180.0),width/2.0,height/2.0,0,0,0,1);
    //  camera(width/2.0,height/2.0,3000,width/2.0,height/2.0,0,0,0,1);
     //camera(0,2000,2000,0,0,500,0,1,0);
     camera(0,camDist * cos(camAngle * PI / 180.0f), camDist * sin(camAngle * PI / 180.0f), 0, 0, 500, 0, 1, 0);
     noStroke();
     lights();

     //  Draw the ground plane
     noStroke();
     fill(150,100,50);
     beginShape();
     vertex(1000,1000,0);
     vertex(-1000,1000,0);
     vertex(-1000,-1000,0);
     vertex(1000,-1000,0);
     endShape(CLOSE);
     
     //  Draw bullseye
     fill(100,0,0);
     sphere(10);
     noFill();

     //  Update the timer
     gameTimer += timeScale / 60.0f;
     thisTime = (int) gameTimer;

     //  Plot CAP1
     CAP1.draw(thisTime, history);
     CAP2.draw(thisTime, history);
     DRP10A.draw(thisTime, history);
     DRP12A.draw(thisTime, history);
     DRP14B.draw(thisTime, history);
     DRP15.draw(thisTime, history);
     DRP06.draw(thisTime, history);
}
class Player{
  
  private int lastPt;
  private int firstPt;
  public ArrayList<Track> track = new ArrayList<Track>();
  public int offset;
  private boolean single;
  private int r;
  private int g;
  private int b;
  private Track pos;
  private boolean show;
  
  public Player(){
  }
  
  public Player(String filename, float posScale, boolean single, boolean show, int r, int g, int b){
    String record;
    String recData[];
    BufferedReader poFile = null;

    this.single = single;
    this.r = r;
    this.g = g;
    this.b = b;
    this.show = show;
    
    try {
      poFile = createReader(filename);
      
      while ((record = poFile.readLine()) != null) {
        recData = record.split(",");
        
        track.add(new Track(recData[0],
            Integer.parseInt(recData[1]),
            Float.parseFloat(recData[2]) * posScale,
            Float.parseFloat(recData[3]) * -posScale,
            Float.parseFloat(recData[4]) * 0.1f
            )
        );
      }
    } catch (IOException err) {
      err.printStackTrace();
    } finally {
      try {
        if (poFile != null) poFile.close();
      } catch (IOException err2) {
        err2.printStackTrace();
      }
    }
    
    this.firstPt = 0;
    this.lastPt = 0;
    this.offset = track.get(0).offset;
    
    println(filename,this.offset);
  }
  
  public void show(){
    this.show = true;
  }
  
  public void hide(){
    this.show = false;
  }
  
  public void draw(int currentTime, int tailLength) {
    if (show) {
      try {
        //  Move the tail pointer to the tail length
        while (track.get(lastPt).offset < currentTime - tailLength) {
          lastPt++;
        }
        
        //  Move the head pointer to the current time
        while (track.get(firstPt).offset < currentTime) {
          firstPt++;
        }
        
        //  If we get here, then we have a set of data to plot
        if (single) {
          
            //  Draw the tail
            noFill();
            stroke(r,g,b);
            beginShape();
            pos = track.get(lastPt);
            curveVertex(pos.x, pos.y, pos.z);
            for (int pt = lastPt; pt <= firstPt; pt++){
               pos = track.get(pt);
               curveVertex(pos.x, pos.y, pos.z);
            }
            curveVertex(pos.x,pos.y,pos.z);
            endShape();
             
            //  Draw the altitude sticks
            stroke(50,50,50);
            for (int pt = lastPt; pt <= firstPt; pt++){
               if (pt % 100 == 0) {
                  pos = track.get(pt);
                  line(pos.x,pos.y,pos.z,pos.x,pos.y,0);
               }
            }
  
            //  Draw the current location
            pos = track.get(firstPt);
            line(pos.x,pos.y,pos.z,pos.x,pos.y,0);
            pushMatrix();
            noStroke();
            fill(r,g,b);
            translate(pos.x, pos.y, pos.z);
            sphere(10);
            popMatrix();
        } else {
          //  Multiple tracks
          
          noStroke();
          fill(r,g,b,150);
          IntDict targets = new IntDict();
          
          for (int pt = firstPt; pt >= lastPt; pt--) {
            pos = track.get(pt);
            //  Has this target been rendered?
            if (!targets.hasKey(pos.localTrack)) {
                targets.set(pos.localTrack,1);
                pushMatrix();
                translate(pos.x, pos.y, pos.z);
                sphere(15);
                popMatrix();
            }
          }
        }
      } catch (IndexOutOfBoundsException idx) {
        //  No more data to plot
      }
    }
  }
}

class Track {
  public String localTrack;
  public int offset;
  public float x;
  public float y;
  public float z;
  
  public Track(String localTrack, int offset, float x, float y, float z) {
    this.localTrack = localTrack;
    this.offset = offset;
    this.x = x;
    this.y = y;
    this.z = z;
  }

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Sketch" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
