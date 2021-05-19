export enum UserRole {
  User = 'USER',
  Manager = 'MANAGER',
}

export type User = {
  role: UserRole;
};
