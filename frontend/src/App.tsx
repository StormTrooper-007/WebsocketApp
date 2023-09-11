import axios from "axios";
import { useState, useEffect } from "react";


function App() {

  const [websocket, setWebSocket] = useState<WebSocket>()
  const [message, setMessage] = useState<string>("")
  
 

  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8080/api/ws/chat")
    ws.onmessage = onMessage
    setWebSocket(ws)
  },[])

  function sendMessage(message:string){
  websocket?.send(message)
  }

  function onMessage(){
    console.log("new message recieved")
  }


  return (
    <form onSubmit={() => sendMessage(message)}>
      <input value={message} onChange={(e) => setMessage(e.target.value)}/>
      <button >send</button>
    </form>
  )
}

export default App
