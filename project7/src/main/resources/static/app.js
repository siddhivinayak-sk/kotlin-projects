var ws = null;
var url = "ws://localhost:8080/event-emitter";
var score = "http://localhost:8080/score";

function setConnected(connected)
{
  document.getElementById('connect').disabled = connected;
  document.getElementById('disconnect').disabled = !connected;
  document.getElementById('echo').disabled = !connected;
  document.getElementById('clear').disabled = !connected;
}

function connect()
{
  ws = new WebSocket(url);
  ws.onopen = function() {
    setConnected(true);
    log('Info: Connection Established.');
  };

  ws.onmessage = function(event) {
    data = JSON.parse(event.data)
    log("Run: " + data.run + "      Over: "
    + data.over + "      Wicket: " + data.wicket + "      Four: "
    + data.four + "      Six: " + data.six + "      Wide: " + data.wide
    + "      No Ball: " + data.noball);
  };

  ws.onclose = function(event) {
    setConnected(false);
    log('Info: Closing Connection.');
  };
}

function disconnect()
{
  if (ws != null) {
    ws.close();
    ws = null;
  }
  setConnected(false);
}

function echo()
{
  if (ws != null)
  {
    var message = document.getElementById('message').value;
    log('Sent to server :: ' + message);
    ws.send(message);
  } else {
    alert('connection not established, please connect.');
  }
}

function clearElements() {
  document.getElementById('message').value = '';
  document.getElementById('logging').innerHTML = '';
}

function log(message)
{
  var console = document.getElementById('logging');
  var p = document.createElement('p');
  p.appendChild(document.createTextNode(message));
  console.appendChild(p);
  console.scrollTop = console.scrollHeight;
}

function ballDetails() {
  var postObj = {
    "wide": document.getElementById('inpWide').checked,
    "noBall": document.getElementById('inpNoBall').checked,
    "wicket": document.getElementById('inpWicket').checked,
    "run": document.getElementById('inpRun').value
  }
  let post = JSON.stringify(postObj)
  let xhr = new XMLHttpRequest()
  xhr.open('PUT', score, true)
  xhr.setRequestHeader('Content-type', 'application/json; charset=UTF-8')
  xhr.setRequestHeader('Accept', '*/*')
  xhr.send(post);

  xhr.onload = function () {
    if(xhr.status === 200) {
        console.log("Post successfully created!")
    }
  }
}

