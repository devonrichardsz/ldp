import React from 'react';
import ReactMarkdown from 'react-markdown';

export interface MarkdownProps {
  text: string;
}

export const Markdown: React.FC<MarkdownProps> = ({ text }: MarkdownProps) => (
  <ReactMarkdown source={text} />
);

export default Markdown;
