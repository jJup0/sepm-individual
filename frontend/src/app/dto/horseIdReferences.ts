import { HorseSex } from "../types/horseSexEnum";

export interface HorseIdReferences {
  id?: number;
  name: string;
  description?: string;
  birthdate: string;
  sex: HorseSex;
  owner?: number;
  mother?: number;
  father?: number;
}
