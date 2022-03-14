import { HorseSex } from "./horseSexEnum";

export interface HorseSearchDto {
  name?: string;
  description?: string;
  bornAfter?: string;
  sex?: HorseSex;
  bornBefore?: Date;
  owner?: string;
}
