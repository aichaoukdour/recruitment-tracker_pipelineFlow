import React from 'react';
import { useForm } from 'react-hook-form';
import './ScheduleInterviewForm.css';
import logo from './logo.jpeg'; 

const ScheduleInterviewForm = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();

  const onSubmit = (data) => {
    console.log('Interview Scheduled:', data);
    // Example: fetch('/api/interviews', { method: 'POST', body: JSON.stringify(data) })
  };

  return (
    <div className="form-container">
      <div className="form-card">
        <img src={logo} alt="Logo" className="logo" />
        <h2 className="form-title">Planifier un entretien</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="form-fields">

          <div className="form-field">
            <label className="form-label">Candidat ID</label>
            <input
              type="text"
              {...register('candidateId', { required: 'Candidate ID is required' })}
              className={`form-input ${errors.candidateId ? 'error-input' : ''}`}
              placeholder="e.g. CAND123"
            />
            {errors.candidateId && <p className="error-message">{errors.candidateId.message}</p>}
          </div>

          <div className="form-field">
            <label className="form-label">Recruteur ID</label>
            <input
              type="text"
              {...register('recruiterId', { required: 'Recruiter ID is required' })}
              className={`form-input ${errors.recruiterId ? 'error-input' : ''}`}
              placeholder="e.g. REC456"
            />
            {errors.recruiterId && <p className="error-message">{errors.recruiterId.message}</p>}
          </div>


          <div className="form-field">
            <label className="form-label">Date d'entretien</label>
            <input
              type="date"
              {...register('date', { required: 'Date is required' })}
              className={`form-input ${errors.date ? 'error-input' : ''}`}
            />
            {errors.date && <p className="error-message">{errors.date.message}</p>}
          </div>

          <div className="form-field">
            <label className="form-label">Heure d'entretien</label>
            <input
              type="time"
              {...register('time', { required: 'Time is required' })}
              className={`form-input ${errors.time ? 'error-input' : ''}`}
            />
            {errors.time && <p className="error-message">{errors.time.message}</p>}
          </div>

          <button
            type="submit"
            className="submit-button"
          >
            Envoyer l'invitation
          </button>
        </form>
      </div>
    </div>
  );
};

export default ScheduleInterviewForm;
