import { useEffect, useState } from 'react';

import { IUser } from 'types/User';

export const useUserById = (id: string): IUser | null => {
  const [user, setUser] = useState<IUser | null>(null);
  useEffect(() => {
    // Fetch user from server.
    setUser({
      id,
      name: `TestUser${id}`,
      avatarUrl: 'https://miro.medium.com/fit/c/128/128/1*Dg5Nl8BJYGqNAjxsoUwCng.jpeg',
      about: 'Lorem Ipsum',
      following: [],
    });
  }, [id]);
  return user;
};
