import { UserRole } from './user.model';

export type JwtPayload = {
  role: UserRole;
};

export function isJwtPayload(value: unknown): value is JwtPayload {
  return (
    value != null &&
    Object.values(UserRole).includes((value as JwtPayload)?.role)
  );
}
