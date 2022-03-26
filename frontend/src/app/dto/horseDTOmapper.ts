import { Horse } from "./horse";
import { HorseIdReferences } from "./horseIdReferences";

// eslint-disable-next-line prefer-arrow/prefer-arrow-functions
export function dtoToIdReferences(horse: Horse): HorseIdReferences {
  if (horse === null) {
    return null;
  }
  const horseIdRefs: HorseIdReferences = {
    id: horse.id,
    name: horse.name,
    description: horse.description,
    birthdate: horse.birthdate,
    sex: horse.sex,
  };

  if (horse.owner) {
    horseIdRefs.owner = horse.owner.id;
  }
  if (horse.mother) {
    horseIdRefs.mother = horse.mother.id;
  }
  if (horse.father) {
    horseIdRefs.father = horse.father.id;
  }
  return horseIdRefs;
}
