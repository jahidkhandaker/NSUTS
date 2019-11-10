var firebaseConfig = {
    apiKey: "AIzaSyCm76SzUIiUon9KEIbAq7iP_i5vImbw7_Y",
    authDomain: "whatsup-f9fab.firebaseapp.com",
    databaseURL: "https://whatsup-f9fab.firebaseio.com",
    projectId: "whatsup-f9fab",
    storageBucket: "whatsup-f9fab.appspot.com",
    messagingSenderId: "1008863225482",
    appId: "1:1008863225482:web:2ef94b2f8cdd1139186f73"
  };
  // Initialize Firebase
firebase.initializeApp(firebaseConfig);


	function stopageActivity(){
		var database = firebase.database();
		var starCountRef = firebase.database().ref('busId');
		const select = document.getElementById("bs");
		
			starCountRef.once('value', function(snapshot) {
			  snapshot.forEach(function(childSnapshot) {
			  
			    var childKey = childSnapshot.key;
			 	select.options[select.options.length] = new Option(childKey, childKey);
			    //var childData = childSnapshot.child("email").val();
			    //document.write(childKey);
			  });
			});
	}

	function writeUserData() {
		const myForm = document.getElementById("busForm");
		const busid = myForm.elements[0].value;
	  
	  firebase.database().ref('busId').child(busid).set({
	    AvailableSeat: "30",
	    location: {
	    	latitude : "23.4228983",
         	longitude : "90.084698"
	    },
	    onBus: "00",
	    running: false,
	    tohome: false,
	    tonsu: false,
	  });

	}

	function addStopage() {
		const myForm = document.getElementById("stopageForm");
		const busid = myForm.elements["bs"].value;
	  	for (var i = 1; i < myForm.length - 1; i++) {
	  	const stp = myForm.elements[i].value;
	  	if (stp != "Name" && busid != "SelectBus") {
	  		firebase.database().ref('busId').child(busid).child("stopage").child(stp).set("00");
	  	}
	  }

	}

	function addFare(){

	}

	function addRfid(){
		const myForm = document.getElementById("RfidForm");
		const rfid = myForm.elements[0].value;
		//const uid = myForm.elements[1].value;
	  	const uid = "jahid"
	  firebase.database().ref('rfid').child(rfid).set({
	    busRfid: "",
	   	truth: false,
	   	uid: uid
	  });

	}

	 function ViewComplain(){
			var starCRef = firebase.database().ref('Complain');
			starCRef.once('value', function(snapshot) {
			  snapshot.forEach(function(childSnapshot) {
			    var childKey = childSnapshot.key;
			    var childEmail = childSnapshot.child("email").val();
			    var childText = childSnapshot.child("text").val();
				
				var table = document.getElementById("complain");
				var row = table.insertRow(1);
	  			var cell1 = row.insertCell(0);
	  			var cell2 = row.insertCell(1);
			   	cell1.innerHTML = childEmail;
			   	cell2.innerHTML = childText;
			  });
			});
	}

	function ViewSchedule(){
			var starSRef = firebase.database().ref('schedule');
			starSRef.once('value', function(snapshot) {
			  snapshot.forEach(function(childSnapshot) {
			    var BusKey = childSnapshot.key;
			    var BusFrom = childSnapshot.child("from").val();
			    var BusTo   = childSnapshot.child("to").val();
			    var BusTime = childSnapshot.child("time").val();
				
				var table = document.getElementById("schedule");
				var row = table.insertRow(1);
	  			var cell1 = row.insertCell(0);
	  			var cell2 = row.insertCell(1);
	  			var cell3 = row.insertCell(2);
	  			var cell4 = row.insertCell(3);
			   	cell1.innerHTML = BusKey;
			   	cell2.innerHTML = BusFrom;
			   	cell3.innerHTML = BusTo;
			   	cell4.innerHTML = BusTime;
			  });
			});
	}


	
	

				