import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { UserNotification } from "../types/responseMessage";

@Injectable({
  providedIn: "root",
})
export class UserNotificationService {
  // messages: UserNotification[] = [];
  messagesSubject = new Subject<UserNotification>();

  constructor() {}

  addNotification(notifaction: UserNotification) {
    this.messagesSubject.next(notifaction);
  }

  getNotifications(): Observable<UserNotification>{
    return this.messagesSubject.asObservable();
  }
}
