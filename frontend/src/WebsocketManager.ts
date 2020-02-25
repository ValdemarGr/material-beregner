import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {Observable} from "rxjs";

const subject: WebSocketSubject<unknown> = webSocket("ws://localhost:8080/ws");

subject.subscribe(
    msg => console.log('message received: ' + msg), // Called whenever there is a message from the server.
    err => console.log(err), // Called if at any point WebSocket API signals some kind of error.
    () => console.log('complete') // Called when connection is closed (for whatever reason).
);

const asObs: Observable<unknown> = subject.asObservable();



export function WebsocketObservable<T>(): Observable<T> { return subject.asObservable() as Observable<T> };
export function Publish<T>(x: T): void { return subject.next(x); };