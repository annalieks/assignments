import React, { useState, useRef } from 'react';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import './App.css';

let isConnected = false;

const App = () => {
  const [events, setEvents] = useState([]);
  const scrollTarget = useRef(null);

  const socket = new SockJS('http://localhost:8080/websocket');
  const stompClient = Stomp.over(socket);
  const onError = (error) => {
    console.log('Error ', error);
  }

  const onConnected = () => {
    const onMessageReceived = (payload) => {
      const recievedMessage = JSON.parse(payload.body);
      setEvents((previousState => ([...previousState, recievedMessage])));
    }
    if (!isConnected) {
      stompClient.subscribe('/topic/events', onMessageReceived);
      isConnected = true;
    }
  }

  stompClient.connect({}, onConnected, onError);

  React.useEffect(() => {
    if (scrollTarget.current) {
      scrollTarget.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [events.length]);

  console.log(events);

  return (
    <div className="App">
      <header>Сервер</header>
      {events.map(e => (
        <div className='Event'>
          <p className="Type">{e.type}: </p>
          <p className="Message">From: {e.message.sender}
            {e.message.recipient !== null ? `, To: ${e.message.recipient}` : ''},
            Message: {e.message.content}</p>
        </div>))}
      <div ref={scrollTarget} />
    </div>
  );
}

export default App;
