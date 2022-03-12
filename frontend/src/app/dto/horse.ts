import { HorseSex } from "./horseSexEnum";

export interface Horse {
  id?: number;
  name: string;
  description?: string;
  birthdate: Date;
  sex: HorseSex;
  owner?: string;
  motherId?: number;
  fatherId?: number;
}
