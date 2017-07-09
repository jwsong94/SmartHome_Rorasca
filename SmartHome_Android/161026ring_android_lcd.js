var express = require('express');
var bodyParser = require('body-parser');

var GPIO = require('onoff').Gpio,		// led
led = new GPIO(20, 'out');

var dht = require('node-dht-sensor');		//Temperature
dht.initialize(11,21);

var wpi = require('wiring-pi');
wpi.setup('gpio');
wpi.wiringPiSetup();
wpi.pinMode(2, wpi.INPUT);   //  switch control
var switch_state = 'off';

var sleep = require('sleep');	// GasValve
//wpi.softPwmCreate(1, 0 ,200);
var GasFlag = true;


var fs = require('fs');		// LCD
var Lcd = require('lcd'),
 lcd = new Lcd({rs: 17, e: 27, data: [22, 23, 24, 25], cols: 16, rows: 2});


var exec = require('child_process').exec, child;


/*
var SerialPort = require('serialport').SerialPort,	// serial
	serial = new SerialPort('/dev/ttyACM0',{
	baudrate:9600
});
 */


var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));



var net = require('net');
function getConnection(connName){
  var client = net.connect({port: 7000, host:'172.20.10.7'}, function() {
    console.log(connName + ' Connected: ');
    console.log('   local = %s:%s', this.localAddress, this.localPort);
    console.log('   remote = %s:%s', this.remoteAddress, this.remotePort);
    this.setTimeout(500);
    this.setEncoding('utf8');

    this.on('data', function(data) {
      console.log(connName + " From Server: " + data.toString());
      this.end();
    });

    this.on('end', function() {
      console.log(connName + ' Client disconnected');
    });

    this.on('error', function(err) {
      console.log('Socket Error: ', JSON.stringify(err));
    });

    this.on('timeout', function() {
      console.log('Socket Timed Out');
    });

    this.on('close', function() {
      console.log('Socket Closed');
    });
  });
  return client;
}


var tcp_input = "1/1";


/* TCP server */

var server = net.createServer(function(client) {	
  console.log('Client connection: ');
  console.log('   local = %s:%s', client.localAddress, client.localPort);
  console.log('   remote = %s:%s', client.remoteAddress, client.remotePort);
  client.setTimeout(500);
  client.setEncoding('utf8');

  /*INPUT DATA  */

  client.on('data', function(data) {		
    console.log('Received data from client on port %d: %s',
                client.remotePort, data.toString());
    console.log('  Bytes received: ' + client.bytesRead);
    writeData(client, 'Sending: ' + data.toString());
    tcp_input = data.toString();
    console.log('  Bytes sent: ' + client.bytesWritten);
      
    
  });

  client.on('end', function() {
    console.log('Client disconnected');
    server.getConnections(function(err, count){
      console.log('Remaining Connections: ' + count);
    });
  });

  client.on('error', function(err) {
    console.log('Socket Error: ', JSON.stringify(err));
  });

  client.on('timeout', function() {
    console.log('Socket Timed out');
  });

});

server.listen(7000, function() {
  console.log('Server listening: ' + JSON.stringify(server.address()));
  server.on('close', function(){
    console.log('Server Terminated');
  });

  server.on('error', function(err){
    console.log('Server Error: ', JSON.stringify(err));
  });

});

function writeData(socket, data){
  var success = !socket.write(data);
  if (!success){
    (function(socket, data){
      socket.once('drain', function(){
        writeData(socket, data);
      });
    })(socket, data);
  }
}


/* Android server */


app.get('/', showMovieList);
app.get('/post', showNewMovieForm);
app.post('/', handlePostRequest);
app.get('/img', imgUpload);

app.listen(7500, function() {
   console.log('Server is listening @ 7500');
});

var Bellflag = true;
var RobotFlag = true;
var playAlert;
var playAlert2;

var hopeTemp = 20; 
var nowTemp = 30;
var temp1 = 0;

var gas;
var gas1;

function imgUpload(req, res){	/* Upload image */
    res.sendfile('/home/pi/Image/Image.jpg');
    console.log("image send file");
}

function handlePostRequest(req, res)
{	//post
   
   console.log(req.body); 
//   console.log(tcp_input);

    var feed_on = req.body.feed_on;
    if(feed_on == 'on')
    {
	console.log("feed on");
	serial.write("O");

    }
    else if(feed_on == 'off')
    {
	console.log("feed off");
	serial.write("X");
    }


    var img_state = req.body.img;
    if(img_state == 'on')
    {
	res.sendfile('images/camera.jpg');
	console.log("post send");

    }



 
   var led_state = req.body.led;	// led
   var temp = req.body.temp;		// temperature
   var bell_state = req.body.bell;	// bell
   var bell = req.body.bell1;


   gas = req.body.gas;  		// gas
   var gas_state = req.body.gas_state;
//   console.log(gas);   
   
   var Robot_r = req.body.r;		// robot
   var Robot_l = req.body.l;
   var Robot_d = req.body.d;
   var Robot_u = req.body.u;
   var Robot_s = req.body.s;
   
   var Robot_reset = req.body.Robot_reset;
   var Robot_live = req.body.live;
   var Robot_refresh = req.body.Robot_refresh;



   function SendData(data){
    try{

    //    res.setHeader('Content-Type', 'application/x-www-form-urlencoded');
	console.log(data);
	console.log("after data");
	res.send(data);
	console.log("send");
	res.end(data);
	console.log("end");

    } catch(exception){
        console.log(exception)
    }
   }


    var data_state = req.body.data_state;
    if(data_state == 'on')
    {
	console.log("data state on");
	var data1;
	 fs.readFile('/home/pi/Image/Image.txt', 'utf8', function(err, data){
	console.log("send : "+data);
	data1 = data;
	SendData(data1);
	});
    }



   var tempup = req.body.TempUp;
   var tempdown = req.body.TempDown;
   
  

  if(tempup == 'on')
  {
	console.log("tempUp");
	hopeTemp = hopeTemp + 1;

	var temp3 = hopeTemp;
	lcd.clear();
	lcd.setCursor(0,0);
	lcd.print("now : "+nowTemp);
	
	setTimeout(function(){
		lcd.setCursor(0,1);
		lcd.print("hope : "+hopeTemp);
		},100);

	console.log("now Temperature : "+nowTemp+
		" hope Temperature : "+hopeTemp);
	SendData(temp3);
  }
	
  if(tempdown == 'on')
  {
      lcd.clear();
      console.log("tempdown");
      hopeTemp = hopeTemp - 1;	
      if(hopeTemp < 10) hopeTemp = 10;

      var temp3 = hopeTemp;
      lcd.setCursor(0,0);
      lcd.print("now : "+nowTemp);

      setTimeout(function(){
	      lcd.setCursor(0,1);
	      lcd.print("hope : "+hopeTemp);

	      },100);
	      console.log("now Temperature : "+nowTemp+
		      " hope Temperature : "+hopeTemp);

	      SendData(temp3);
  }	


   if(Robot_u == 'on')
  {
 	var up = getConnection("up");
	console.log("Robot up");
	writeData(up,"u");
  }   

   if(Robot_d == 'on')
  {
 	var down = getConnection("down");
	console.log("Robot down");
	writeData(down,"d");
  }   
   if(Robot_l == 'on')
  {
 	var left = getConnection("left");
	console.log("Robot left");
	writeData(left,"l");
  }   
   if(Robot_r == 'on')
  {
 	var right = getConnection("right");
	console.log("Robot right");
	writeData(right,"r");
  }   
   if(Robot_s == 'on')
  {
 	var stop = getConnection("stop");
	console.log("Robot stop");
	writeData(stop,"s");
  } 
   if(Robot_reset == 'on')
   {
       var reset = getConnection("reset");
       console.log("Robot reset");
       writeData(reset,"b");

   }
 


  if((Robot_live == 'on') && (RobotFlag == true))
  {
    RobotFlag = false;
        playAlert2 =  setInterval( function()
       {
                console.log("In setInterval");
	
		SendData(tcp_input);
       }, 1000);
  }

  else if(Robot_live == 'off')
  {
	RobotFlag = true;
	clearInterval(playAlert2);
  }

  if(Robot_refresh == 'on')
  {
    SendData(tcp_input);


  }


  if(bell == 'on')
  {
      console.log("in bell");
   if(wpi.digitalRead(2) == wpi.HIGH)
   {
       console.log("bellllllllllll");
       SendData("bell");

    
   } 

  } 
  else if(bell == 'off')
  {
      console.log("bell off");

  }



  if(( bell_state == 'on') && (Bellflag == true))
  {
	Bellflag = false; 
	console.log("bell_state on"); 
        playAlert =  setInterval(function()
       {
		console.log("In setInterval");
     		if((wpi.digitalRead(2) == wpi.HIGH) && (bell_state =='on'))
  		{	
			console.log('bell on');

//			res.setHeader('Content-Type', 'application/x-www-form-urlencoded');
//			res.send("switchON");
//			clearInterval(playAlert);
			console.log('clear Interval ');
 		 }
       }, 500);
	console.log(" finish bell_state : " + bell_state);
  }

   else if(bell_state == 'off')
  {
	console.log("bell off");
	Bellflag = true;
	clearInterval(playAlert);
  }

   if(led_state == 'on')
  { 
	console.log('led on');
	led.writeSync(1);
	
	
  }

   if(led_state == 'off') 
  {
     led.writeSync(0);
     console.log('led off');
  }

  if(temp == 'Temperature Check')
  {
	var getTemp = dht.read();
	temp1 = getTemp.temperature;
	var temp2 = getTemp.humidity;
	
//	res.setHeader('Content-Type', 'application/x-www-form-urlencoded');
//	res.send(temp1+"/"+temp2);
	
	SendData(temp1+"/"+temp2);

	console.log('temp check');
	nowTemp = temp1;	

	//lcd.setCursor(0,0);
	//lcd.print("now Temperature : "+temp1+
	//	" hope Temperature : "+hopeTemp);
  }

  if(gas == 'on')
  {
	if(GasFlag)
	{
//	    serial.write("G");
	    
		child = exec("sudo node servo_hw.js", function(error, stdout, stderr)
        	{
               		 console.log(stdout);
               		 if(error != null)
               		 {
                	        console.log('exec error : ' + error);
               		 }
	        });
		console.log("gas on");
		GasFlag = false;
//		wpi.softPwmWrite(1, 10);
//		wpi.pwmWrite(1,70);
//		sleep.usleep(5000000);
//		sleep.sleep(1);
//		wpi.softPwmWrite(1, 0);
//		sleep.sleep(1);
//		gas="";
	   
	}
  }
  
  else if(gas == 'off')
  {
	if(!GasFlag)
	{
	    
	//    serial.write("g");
	    
		 child = exec("sudo node servo_hw2.js", function(error, stdout, stderr)
	        
        	{
               		 console.log(stdout);
               		 if(error != null)
               		 {
                	        console.log('exec error : ' + error);
               		 }
	        });

	
		console.log("gas off");
//		wpi.softPwmWrite(1, 30);
//		wpi.pwmWrite(1,24);
		GasFlag = true;
//		sleep.sleep(1);
//		gas="";
	    
	}
  }

  if(gas_state == 'on')
  {
	console.log(gas);
	console.log(GasFlag);
//	res.setHeader('Content-Type', 'application/x-www-form-urlencoded');
//	res.send(GasFlag);	 
	
//	res.send(GasFlag);	 
	
  } 

 

}





/*


serial.on('data', function(data){
	console.log(data.toString());
	if(data == 'M')
	{
	
		child = exec("sudo ./Capture", function(error, stdout, stderr)
			{
			console.log("1");
		if(error != null)
		{
		console.log("exec error");
    
		}
		});	
		
	console.log("MMMMMMMMMMMMMMMMM");
	}
    console.log("if out");
});
*/		
		
function showNewMovieForm(req, res) {
	console.log('showNewMovieForm first');
}

function showMovieList(req, res) {  
	console.log('showMovieList first');
}
