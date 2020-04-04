/**
 * Header
 */
import React from 'react';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { ConfigProvider, Dropdown, Menu } from '@alifd/next';
import siteConfig from '../config';
import { changeLanguage } from '@/reducers/locale';
import PasswordReset from '../pages/AuthorityControl/UserManagement/PasswordReset';
import { passwordReset } from '../reducers/authority';

import './index.scss';

@withRouter
@connect(state => ({ ...state.locale }), { changeLanguage })
@ConfigProvider.config
class Header extends React.Component {
  static displayName = 'Header';

  static propTypes = {
    locale: PropTypes.object,
    history: PropTypes.object,
    location: PropTypes.object,
    language: PropTypes.string,
    changeLanguage: PropTypes.func,
  };

  state = { passwordResetUser: '' };

  switchLang = () => {
    const { language = 'en-US', changeLanguage } = this.props;
    const currentLanguage = language === 'en-US' ? 'zh-CN' : 'en-US';
    changeLanguage(currentLanguage);
  };

  logout = () => {
    window.localStorage.clear();
    this.props.history.push('/login');
  };

  changePassword = () => {
    this.setState({
      passwordResetUser: this.getUsername(),
    });
  };

  getUsername = () => {
    const token = window.localStorage.getItem('token');
    if (token) {
      const [, base64Url = ''] = token.split('.');
      const base64 = base64Url.replace('-', '+').replace('_', '/');
      try {
        const parsedToken = JSON.parse(window.atob(base64));
        return parsedToken.sub;
      } catch (e) {
        delete localStorage.token;
        location.reload();
      }
    }
    return '';
  };

  render() {
    const {
      locale = {},
      language = 'en-us',
      location: { pathname },
    } = this.props;
    const { passwordResetUser = '' } = this.state;
    const { home, docs, languageSwitchButton } = locale;
    const HOME_URL = '/';
    const DOCS_URL =
      'http://note.youdao.com/noteshare?id=3ede00878261f772fa8b20c6fee2bd98&sub=3D1C18';
    const NAV_MENU = [
      { id: 1, title: home, link: HOME_URL },
      { id: 2, title: docs, link: DOCS_URL },
    ];
    return (
      <>
        <header className="header-container header-container-primary">
          <div className="header-body">
            {/* <a
              href={`https://nacos.io/${language.toLocaleLowerCase()}/`}
              target="_blank"
              rel="noopener noreferrer"
            >
              <img
                src="img/logo-2000-390.svg"
                className="logo"
                alt={siteConfig.name}
                title={siteConfig.name}
              />
            </a> */}
            {/* if is login page, we will show logout */}
            {pathname !== '/login' && (
              <Dropdown trigger={<div className="logout">{this.getUsername()}</div>}>
                <Menu>
                  <Menu.Item onClick={this.logout}>{locale.logout}</Menu.Item>
                  <Menu.Item onClick={this.changePassword}>{locale.changePassword}</Menu.Item>
                </Menu>
              </Dropdown>
            )}
            <span className="language-switch language-switch-primary" onClick={this.switchLang}>
              {languageSwitchButton}
            </span>
            <div className="header-menu header-menu-open">
              <ul>
                {NAV_MENU.map(item => (
                  <li key={item.id} className="menu-item menu-item-primary">
                    <a href={item.link} target="_blank" rel="noopener noreferrer">
                      {item.title}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </header>
        <PasswordReset
          username={passwordResetUser}
          onOk={user =>
            passwordReset(user).then(res => {
              return res;
            })
          }
          onCancel={() => this.setState({ passwordResetUser: undefined })}
        />
      </>
    );
  }
}

export default Header;
