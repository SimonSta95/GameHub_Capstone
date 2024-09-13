import { Link } from 'react-router-dom';
import { Avatar } from '@mui/material';
import logo from '../../assets/gamehub-color.png';
import './Header.css';
import { User } from "../../types.ts";

type HeaderProps = {
    user?: User | null;
    onLogin: () => void;
    onLogout: () => void;
};

export default function Header(props: Readonly<HeaderProps>) {
    return (
        <header className="header">
            <Link to="/" className="logo-link">
                <img className="logo" src={logo} alt="GameHub Logo" />
            </Link>

            <nav className="nav">
                {props.user && <Link to="/games" className="nav-link">Games</Link>}
                {props.user && <Link to="/my-library" className="nav-link">My Library</Link>}

                {props.user ? (
                    <div className="user-info">
                        <Link to={`/user/${props.user.id}`} className="user-link">
                            <Avatar className="avatar" alt={props.user.username} src={props.user.avatarUrl} />
                        </Link>
                        <div className="username-tooltip">{props.user.username}</div>
                        <button className="auth-button" onClick={props.onLogout}>Logout</button>
                    </div>
                ) : (
                    <>
                        <button className="auth-button" onClick={props.onLogin}>GitHub Login</button>
                        <Link to="/login">
                            <button className="auth-button">Normal Login</button>
                        </Link>
                    </>
                )}
            </nav>
        </header>
    );
}
