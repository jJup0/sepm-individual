import { HorseSex } from "./horseSexEnum";
import { Owner } from "./owner";

export interface Horse {
  id?: number;
  name: string;
  description?: string;
  birthdate: Date;
  sex: HorseSex;
  owner?: Owner;
  mother?: Horse;
  father?: Horse;
}
