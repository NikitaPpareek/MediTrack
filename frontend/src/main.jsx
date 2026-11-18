import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Activity, CalendarDays, CheckCircle2, CircleUserRound, ClipboardList, HeartPulse, Plus, Search, Trash2, Users, X } from 'lucide-react';
import './styles.css';

const API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const emptyForm = { fullName: '', dateOfBirth: '', gender: 'FEMALE', email: '', phone: '', notes: '' };

function App() {
  const [patients, setPatients] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [summary, setSummary] = useState({ totalPatients: 0, totalAppointments: 0, scheduledAppointments: 0, completedAppointments: 0, appointmentsToday: 0 });
  const [query, setQuery] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function request(path, options = {}) {
    const response = await fetch(`${API}${path}`, { headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }, ...options });
    if (!response.ok) {
      let message = `Request failed (${response.status})`;
      try { const body = await response.json(); message = body.detail || body.message || body.error || message; } catch {}
      throw new Error(message);
    }
    if (response.status === 204) return null;
    const text = await response.text();
    return text ? JSON.parse(text) : null;
  }

  async function load(search = query) {
    setError('');
    try {
      const suffix = search.trim() ? `?q=${encodeURIComponent(search.trim())}` : '';
      const [p, a, s] = await Promise.all([request(`/patients${suffix}`), request('/appointments'), request('/reports/summary')]);
      setPatients(p); setAppointments(a); setSummary(s);
    } catch (e) { setError(`${e.message}. Is the Spring Boot backend running on port 8080?`); }
  }

  useEffect(() => { load(''); }, []);
  useEffect(() => { const timer = setTimeout(() => load(query), 250); return () => clearTimeout(timer); }, [query]);

  async function addPatient(event) {
    event.preventDefault(); setBusy(true); setError(''); setNotice('');
    try {
      await request('/patients', { method: 'POST', body: JSON.stringify(form) });
      setForm(emptyForm); setShowForm(false); setNotice('Patient record added successfully.'); await load(query);
    } catch (e) { setError(e.message); }
    finally { setBusy(false); }
  }

  async function removePatient(patient) {
    if (!window.confirm(`Delete patient record for ${patient.fullName}?`)) return;
    setError(''); setNotice('');
    try { await request(`/patients/${patient.id}`, { method: 'DELETE' }); setNotice('Patient record deleted.'); await load(query); }
    catch (e) { setError(e.message); }
  }

  const upcoming = useMemo(() => appointments.filter(a => a.status === 'SCHEDULED').sort((a,b) => (a.appointmentDateTime || '').localeCompare(b.appointmentDateTime || '')).slice(0, 5), [appointments]);
  const fmtDate = value => value ? new Date(value).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' }) : '—';

  return <div className="app-shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><HeartPulse size={23}/></div><div><strong>MediTrack</strong><span>CARE OPERATIONS</span></div></div>
      <div className="workspace-label">WORKSPACE</div>
      <button className="nav-item active"><Activity size={18}/> Overview</button>
      <button className="nav-item" onClick={() => document.getElementById('patients')?.scrollIntoView({ behavior: 'smooth' })}><Users size={18}/> Patients</button>
      <button className="nav-item" onClick={() => document.getElementById('appointments')?.scrollIntoView({ behavior: 'smooth' })}><CalendarDays size={18}/> Appointments</button>
      <button className="nav-item" onClick={() => document.getElementById('reports')?.scrollIntoView({ behavior: 'smooth' })}><ClipboardList size={18}/> Reports</button>
      <div className="sidebar-bottom"><div className="profile"><div className="avatar">NP</div><div><strong>Admin Workspace</strong><span>Demo environment</span></div></div><p>Portfolio project · Synthetic data only</p></div>
    </aside>

    <main className="main-content">
      <header className="topbar"><div><span className="eyebrow">HEALTHCARE MANAGEMENT</span><h1>Overview</h1></div><div className="topbar-right"><span className="live-dot"></span><span>System overview</span><div className="user-icon"><CircleUserRound size={22}/></div></div></header>
      <section className="welcome"><div><span className="welcome-tag">MONITOR · MANAGE · IMPROVE</span><h2>Good care starts with clear information.</h2><p>Manage patient records, track appointments, and review operational metrics in one place.</p></div><div className="welcome-icon"><HeartPulse size={44}/></div></section>

      {error && <div className="alert error"><span>{error}</span><button onClick={() => setError('')} aria-label="Dismiss error"><X size={16}/></button></div>}
      {notice && <div className="alert success"><CheckCircle2 size={17}/><span>{notice}</span><button onClick={() => setNotice('')} aria-label="Dismiss message"><X size={16}/></button></div>}

      <section className="stats-grid" id="reports">
        <Stat icon={<Users size={20}/>} label="Total patients" value={summary.totalPatients} tone="teal" note="Registered records"/>
        <Stat icon={<CalendarDays size={20}/>} label="Total appointments" value={summary.totalAppointments} tone="blue" note="All statuses"/>
        <Stat icon={<Activity size={20}/>} label="Scheduled" value={summary.scheduledAppointments} tone="purple" note="Awaiting visit"/>
        <Stat icon={<CheckCircle2 size={20}/>} label="Appointments today" value={summary.appointmentsToday} tone="orange" note="Active bookings"/>
      </section>

      <section className="panel" id="patients">
        <div className="panel-heading"><div><span className="eyebrow">PATIENT DIRECTORY</span><h2>Patient records</h2><p>Search and manage registered patient information.</p></div><button className="primary-button" onClick={() => { setShowForm(!showForm); setError(''); }}><Plus size={17}/>{showForm ? 'Close form' : 'Add patient'}</button></div>
        {showForm && <form className="patient-form" onSubmit={addPatient}>
          <div className="form-title"><strong>New patient</strong><span>Use fictional information for this demo.</span></div>
          <label>Full name<input required maxLength="80" value={form.fullName} onChange={e => setForm({...form, fullName:e.target.value})} placeholder="e.g. Aanya Sharma"/></label>
          <label>Date of birth<input required type="date" max={new Date().toISOString().slice(0,10)} value={form.dateOfBirth} onChange={e => setForm({...form, dateOfBirth:e.target.value})}/></label>
          <label>Gender<select value={form.gender} onChange={e => setForm({...form, gender:e.target.value})}><option value="FEMALE">Female</option><option value="MALE">Male</option><option value="OTHER">Other</option></select></label>
          <label>Email<input required type="email" value={form.email} onChange={e => setForm({...form, email:e.target.value})} placeholder="patient@example.com"/></label>
          <label>Phone<input value={form.phone} onChange={e => setForm({...form, phone:e.target.value})} placeholder="Optional"/></label>
          <label className="wide">Notes<textarea maxLength="500" value={form.notes} onChange={e => setForm({...form, notes:e.target.value})} placeholder="Optional administrative notes" rows="2"/></label>
          <div className="form-actions"><button type="button" className="secondary-button" onClick={() => {setShowForm(false);setForm(emptyForm);}}>Cancel</button><button disabled={busy} className="primary-button" type="submit">{busy ? 'Saving…' : 'Save patient'}</button></div>
        </form>}
        <div className="table-toolbar"><div className="search-box"><Search size={17}/><input value={query} onChange={e => setQuery(e.target.value)} placeholder="Search by name or email"/></div><span className="record-count">{patients.length} records</span></div>
        <div className="table-wrap"><table><thead><tr><th>PATIENT</th><th>DATE OF BIRTH</th><th>GENDER</th><th>CONTACT</th><th>ACTION</th></tr></thead><tbody>
          {patients.map(p => <tr key={p.id}><td><div className="patient-cell"><div className="patient-avatar">{p.fullName.split(' ').map(s=>s[0]).slice(0,2).join('').toUpperCase()}</div><div><strong>{p.fullName}</strong><span>PT-{String(p.id).padStart(4,'0')}</span></div></div></td><td>{fmtDate(p.dateOfBirth)}</td><td><span className="gender-pill">{p.gender}</span></td><td><div className="contact-cell"><strong>{p.email}</strong><span>{p.phone || 'No phone added'}</span></div></td><td><button className="icon-button danger" title="Delete patient" onClick={() => removePatient(p)}><Trash2 size={16}/></button></td></tr>)}
          {patients.length === 0 && <tr><td colSpan="5"><div className="empty-state"><Users size={28}/><strong>No patient records yet</strong><span>Add a fictional patient to get started, or check that the backend is running.</span></div></td></tr>}
        </tbody></table></div>
      </section>

      <section className="panel appointments-panel" id="appointments"><div className="panel-heading compact"><div><span className="eyebrow">SCHEDULE OVERVIEW</span><h2>Upcoming appointments</h2><p>Next scheduled visits in the system.</p></div><span className="soft-badge"><CalendarDays size={15}/> {upcoming.length} upcoming</span></div>
        {upcoming.length ? <div className="appointment-list">{upcoming.map(a => <div className="appointment-row" key={a.id}><div className="date-tile"><strong>{new Date(a.appointmentDateTime).getDate()}</strong><span>{new Date(a.appointmentDateTime).toLocaleDateString(undefined,{month:'short'})}</span></div><div className="appointment-detail"><strong>{a.patient?.fullName || 'Patient'}</strong><span>{a.reason || 'General consultation'} · {a.providerName}</span></div><span className="status-pill">Scheduled</span></div>)}</div> : <div className="empty-state appointment-empty"><CalendarDays size={26}/><strong>No upcoming appointments</strong><span>Appointment creation is available through the documented REST API.</span></div>}
      </section>
      <footer>MEDiTRACK · Demo application for software engineering portfolio <span>Not for clinical use</span></footer>
    </main>
  </div>;
}

function Stat({ icon, label, value, tone, note }) { return <div className="stat-card"><div className={`stat-icon ${tone}`}>{icon}</div><div className="stat-label">{label}</div><div className="stat-value">{value}</div><div className="stat-note">{note}</div></div>; }

createRoot(document.getElementById('root')).render(<App/>);
