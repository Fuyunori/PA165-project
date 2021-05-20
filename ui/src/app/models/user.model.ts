export enum UserRole {
  User = 'USER',
  Manager = 'MANAGER',
}

export type User = {
  id: number;
  username: string;
  role: UserRole;
  name: string;
  email: string;
};

export type UnknownUser = Omit<User, 'id'>;