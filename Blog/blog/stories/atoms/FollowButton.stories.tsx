import React from 'react';

import { FollowButton, FollowButtonProps } from 'components/atoms/FollowButton';

export default {
  component: FollowButton,
  title: 'Atoms/Follow Button',
};

const Template = (args: FollowButtonProps) => <FollowButton {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  toFollow: {
    id: '3264',
    name: 'Test User',
    following: [],
  },
};
