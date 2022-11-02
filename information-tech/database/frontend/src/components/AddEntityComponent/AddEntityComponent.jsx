import React from 'react';
import { Icon } from 'semantic-ui-react';
import styles from './AddEntityComponent.module.css';

const AddEntityComponent = ({ name, setName, submit, visible, setVisible, text }) => {

	const onAddClick = () => {
		let inputName = name.trim();
		if (inputName === '') {
			return;
		}
		submit(inputName);
		setVisible(false);
		setName('');
	}

	return visible ? (
		<div className={styles.input_container}>
			<div className={styles.close_container}>
				<button
					className={styles.close_button}
					onClick={() => setVisible(false)}
				>
					<Icon name="close" />
				</button>
			</div>
			<div className={styles.add_header}>{text || 'Enter the Name'}</div>
			<div>
				<input className={styles.add_input}
					type="text"
					value={name}
					onChange={(e) => setName(e.target.value)}
				/>
			</div>
			<div className={styles.submit_container}>
				<button
					className={styles.submit_button}
					onClick={() => onAddClick()}>
					Submit
				</button>
			</div>
		</div>
	) : null;
}

export default AddEntityComponent;