export type Court = {
  id: string;
  name: string;
  address: string;
};

export type UnknownCourt = Omit<Court, 'id'>;
