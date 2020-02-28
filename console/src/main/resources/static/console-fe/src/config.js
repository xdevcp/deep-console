module.exports = {
  server: '',
  PAGESIZE: 15,
  TIMERDEFAULT: '5s',
  TIMEDURINT: 2000,
  is_preview: process.env.NODE_ENV === 'development',
  projectName: 'deep-console',
  defaultLanguage: 'zh-cn',
  'en-us': {
    pageMenu: [
      {
        key: 'home', // 用作顶部菜单的选中
        text: 'HOME',
        link: '/',
      },
      {
        key: 'docs',
        text: 'DOCS',
        link: 'https://nacos.io/en-us/docs/quick-start.html',
      },
    ],
    disclaimer: {
      title: 'Vision',
      content: 'none',
    },
    documentation: {
      title: 'Documentation',
      list: [
        {
          text: 'Overview',
          link: '/en-us/docs/what-is-nacos.html',
        },
        {
          text: 'Quick start',
          link: '/en-us/docs/quick-start.html',
        },
        {
          text: 'Developer guide',
          link: '/en-us/docs/contributing.html',
        },
      ],
    },
    resources: {
      title: 'Resources',
      list: [
        {
          text: 'Community',
          link: '/en-us/community/index.html',
        },
      ],
    },
    copyright: '@2020',
  },
  'zh-cn': {
    pageMenu: [
      {
        key: 'home',
        text: '首页',
        link: '/',
      },
      {
        key: 'docs',
        text: '文档',
        link: 'https://nacos.io/zh-cn/docs/what-is-nacos.html',
      },
    ],
    disclaimer: {
      title: '愿景',
      content: 'none',
    },
    documentation: {
      title: '文档',
      list: [
        {
          text: '概览',
          link: '/zh-cn/docs/what-is-nacos.html',
        },
        {
          text: '快速开始',
          link: '/zh-cn/docs/quick-start.html',
        },
        {
          text: '开发者指南',
          link: '/zh-cn/docs/contributing.html',
        },
      ],
    },
    resources: {
      title: '资源',
      list: [
        {
          text: '社区',
          link: '/zh-cn/community/index.html',
        },
      ],
    },
    copyright: '@2020',
  },
};
