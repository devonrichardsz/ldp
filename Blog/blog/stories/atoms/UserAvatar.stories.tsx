import React from 'react';

import { UserAvatar, UserAvatarProps } from 'components/atoms/UserAvatar';

import { DEFAULT_USER, DEFAULT_USER_NO_AVATAR } from 'defaults/User';

export default {
  component: UserAvatar,
  title: 'Atoms/UserAvatar',
};

const Template = (args: UserAvatarProps) => <UserAvatar {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  user: DEFAULT_USER,
};

export const NoAvatar = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
NoAvatar.args = {
  user: DEFAULT_USER_NO_AVATAR,
};
