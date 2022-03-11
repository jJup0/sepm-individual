import { HorseSex } from "./horseSex";

export interface Horse {
  id?: number;
  name: string;
  description: string;
  birthdate: Date;
  sex: HorseSex;
  owner: string;
}
