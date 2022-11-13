import React, { useEffect, useState, useRef } from 'react';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import './App.css';


const socket = new SockJS('http://localhost:8080/websocket');
const stompClient = Stomp.over(socket);

const onError = (error) => {
  console.log('Error ', error);
}

stompClient.connect({}, () => { }, onError);

const NameInputComponent = ({ userName, setName, setPopupShown, setMessages }) => {
  const onClick = () => {
    if (userName === '') {
      return;
    }

    const onMessageReceived = (payload) => {
      const recievedMessage = JSON.parse(payload.body);
      setMessages((previousState => ([...previousState, recievedMessage])));
    }

    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.subscribe(`/queue/${userName}`, onMessageReceived);

    setPopupShown(false);
  }
  return (<div className='NameComponent'>
    <header>Введіть Ваше ім'я</header>
    <input value={userName} onChange={(e) => setName(e.target.value)} />
    <button onClick={() => onClick()}>Приєднатися</button>
  </div>)
}

const ChatComponent = ({ userName, messages, setMessages }) => {

  const [message, setMessage] = useState('');
  const [recipient, setRecipient] = useState('');

  const sendMessage = () => {
    if (message === '') {
      return;
    }

    const m = {
      content: message,
      recipient: recipient === '' ? null : recipient,
      sender: userName,
    };

    stompClient.send('/app/chat', {}, JSON.stringify(m));

    if (m.recipient != null) {
      setMessages([...messages, m]);
    }
    setMessage('');
    setRecipient('')
  }

  const scrollTarget = useRef(null);

  React.useEffect(() => {
    if (scrollTarget.current) {
      scrollTarget.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages.length]);

  return (<div className='Chat'>
    <div className='ChatHeader'>
      <header>Чат</header>
      <p>{userName}</p>
    </div>
    <div className='ChatContent'>
      {messages.map(m => (
        <div className={`${m.sender === userName ? 'MyMessage' : 'Message'}`}>
          <p className='Sender'>{m.recipient === null ? '' : '(Private)'} From {m.sender}</p>
          <p className='Content'>{m.content}</p>
        </div>
      ))}
      <div ref={scrollTarget} />
    </div>
    <div className='MessageControls'>
      <div>
        <input className='MessageInput'
          placeholder='Введіть повідомлення...'
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <input className='RecipientInput'
          placeholder='Введіть одержувача...'
          value={recipient}
          onChange={(e) => setRecipient(e.target.value)}
        />
      </div>
      <button className='SendButton' onClick={() => sendMessage()}>Відправити</button>
    </div>
  </div>);
}

const App = () => {

  const [userName, setName] = useState('');
  const [messages, setMessages] = useState([]);
  const [popupShown, setPopupShown] = useState(true);

  return (
    <div className="App">
      {popupShown
        ? <NameInputComponent
          userName={userName}
          setName={setName}
          setMessages={setMessages}
          popupShown={popupShown}
          setPopupShown={setPopupShown}
        />
        : <ChatComponent
          userName={userName}
          messages={messages}
          setMessages={setMessages}
        />}
    </div>
  );
}

export default App;
