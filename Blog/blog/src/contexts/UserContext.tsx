import { createContext, useContext } from 'react';

import { IUser } from 'types/User';

const UserContext = createContext<IUser | null>(null);

export const UserProvider = UserContext.Provider;

export const useActiveUser = (): IUser | null => useContext(UserContext);
