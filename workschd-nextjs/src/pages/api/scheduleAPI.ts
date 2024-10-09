import type { NextApiRequest, NextApiResponse } from 'next';


export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const { method } = req
  
    switch (method) {
      case 'GET':
        try {
          const schedules = await prisma.schedule.findMany()
          res.status(200).json(schedules)
        } catch (error) {
          res.status(500).json({ error: 'Failed to fetch schedules' })
        }
        break
      case 'POST':
        try {
          const { title, startTime, endTime, userId } = req.body
          const newSchedule = await prisma.schedule.create({
            data: {
              title,
              startTime: new Date(startTime),
              endTime: new Date(endTime),
              userId: parseInt(userId)
            }
          })
          res.status(201).json(newSchedule)
        } catch (error) {
          res.status(500).json({ error: 'Failed to create schedule' })
        }
        break
      default:
        res.setHeader('Allow', ['GET', 'POST'])
        res.status(405).end(`Method ${method} Not Allowed`)
    }
  }