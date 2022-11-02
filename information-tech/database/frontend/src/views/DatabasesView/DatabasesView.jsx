import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom'
import { ToastContainer, toast } from 'react-toastify';
import { Icon } from 'semantic-ui-react';
import AddEntityComponent from '../../components/AddEntityComponent/AddEntityComponent';
import { DB_SERVICE } from '../../config';
import styles from './DatabasesView.module.css';

const DatabasesView = () => {

	const inputFile = useRef(null);
	const [dbName, setDbName] = useState('');
	const [isAddPopupVisible, setAddPopupVisible] = useState(false);
	const [databases, setDatabases] = useState([]);
	const navigate = useNavigate();

	const exportDatabase = (name, id) => {
		fetch(`${DB_SERVICE}/databases/${id}/export`)
			.then((response) => response.blob())
			.then((blob) => {
				const url = window.URL.createObjectURL(
					new Blob([blob]),
				);
				const link = document.createElement('a');
				link.href = url;
				link.setAttribute(
					'download',
					`${name}.json`,
				);
				document.body.appendChild(link);
				link.click();
				link.parentNode.removeChild(link);
			});
	}

	const getDatabases = () => {
		fetch(`${DB_SERVICE}/databases`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setDatabases(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching databases: ${error}`)
			});
	}

	const getFileExtenstion = (filename) => {
		return filename.split('.').pop();
	}

	const importDatabase = (f) => {
		console.log(f);
		if (getFileExtenstion(f.name) != 'json') {
			toast.error('Provided file is not in the JSON format');
			return;
		}
		const formData = new FormData();
		formData.append('database', f);
		fetch(`${DB_SERVICE}/databases/import`, {
			method: 'POST',
			body: formData
		})
			.then(res => {
				if (res.status !== 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setDatabases([...databases, res]))
			.catch(error => {
				toast.error('Error! ' + error.message);
				console.log(`Error importing a database: ${error}`)
			});
	}

	const addDatabase = (name) => {
		fetch(`${DB_SERVICE}/databases`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ name: name })
		})
			.then(res => {
				if (res.status !== 201) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setDatabases([...databases, res]))
			.catch(error => {
				toast.error('Error! ' + error.message);
				console.log(`Error creating a database: ${error}`)
			});
	}

	const onImportClick = () => {
		inputFile.current.click();
	}

	const onUploadFile = (e) => {
		e.stopPropagation();
		e.preventDefault();
		importDatabase(e.target.files[0]);
		e.target.value = null;
	}

	useEffect(() => {
		getDatabases();
	}, []);

	return (
		<div className={styles.databases_container}>
			<AddEntityComponent
				name={dbName}
				setName={setDbName}
				submit={addDatabase}
				visible={isAddPopupVisible}
				setVisible={setAddPopupVisible} />
			<div className={styles.header}>Databases</div>
			<div className={styles.controls_container}>
				<input type='file' id='file'
					ref={inputFile}
					onChange={(e) => onUploadFile(e)} style={{ display: 'none' }}
					accept="application/JSON"
				/>
				<button
					className={styles.add_button}
					onClick={() => setAddPopupVisible(true)}
				>
					Add Database
				</button>
				<button
					className={styles.import_button}
					onClick={() => onImportClick()}
				>
					Import Database
				</button>
			</div>
			<div className={styles.dblist_container}>
				{databases.map(d => <DatabaseItem
					name={d.name}
					id={d.id}
					exportDatabase={exportDatabase}
					onItemClick={() => navigate(`/database/${d.id}`)}
				/>)}
			</div>
		</div>
	);

}

const DatabaseItem = ({ name, id, exportDatabase, onItemClick }) => {
	return (
		<div className={styles.dbitem_container}>
			<div className={styles.dbitem_name}
				onClick={() => onItemClick()}
			>{name}</div>
			<button className={styles.export_button} onClick={() => exportDatabase(name, id)}>
				<Icon name='download' />
			</button>
		</div>
	);
}



export default DatabasesView;