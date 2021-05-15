export enum CourtType {
  Grass = 'GRASS',
  Clay = 'CLAY',
  Turf = 'TURF',
}

export type Court = {
  id: string;
  name: string;
  address: string;
  type: CourtType;
  previewImageUrl: string;
};

export type UnknownCourt = Omit<Court, 'id'>;
