export type MessageType = "error" | "success" | "warning";

export interface UserNotification {
  message: string;
  type: MessageType;
}
